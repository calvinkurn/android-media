package com.tokopedia.affiliate.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.interfaces.AffiliateInfoClickInterfaces
import com.tokopedia.affiliate.model.response.AffiliateCommissionDetailsData
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateHowToPromoteBottomSheet
import com.tokopedia.affiliate.viewmodel.AffiliateTransactionDetailViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelActivity
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifyprinciples.Typography
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class AffiliateTransactionDetailActivity : BaseViewModelActivity<AffiliateTransactionDetailViewModel>(),AffiliateInfoClickInterfaces {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    private lateinit var affiliateVM: AffiliateTransactionDetailViewModel

    companion object {
        private const val PARAM_TRANSACTION = "PARAM_TRANSACTION"

        fun createIntent(
                context: Context,
                transactionId: String?
        ): Intent {
            val intent = Intent(context, AffiliateTransactionDetailActivity::class.java)
            intent.putExtra(PARAM_TRANSACTION, transactionId)
            return intent
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        afterViewCreated()
    }

    private fun afterViewCreated() {
        initNavBar()
        initRv()
        initObserver()
        getData()
    }

    private fun initNavBar() {
        findViewById<NavToolbar>(R.id.transaction_navToolbar)?.run {
            setOnBackButtonClickListener {
                finish()
            }
        }
    }

    private fun getData() {
        intent?.getStringExtra(PARAM_TRANSACTION)?.let { transactionID ->
            affiliateVM.affiliateCommission(transactionID)
        }
    }

    private val adapter: AffiliateAdapter = AffiliateAdapter(AffiliateAdapterFactory(affiliateInfoClickInterfaces = this))
    lateinit var detailsRV : RecyclerView
    private fun initRv() {
        detailsRV = findViewById(R.id.details_rv)
        detailsRV.layoutManager = LinearLayoutManager(this)
        detailsRV.adapter = adapter
    }


    private fun initObserver() {
        affiliateVM.getErrorMessage().observe(this , {
            hideView()
            showError(it)
        })
        affiliateVM.getCommissionData().observe(this,{
            setData(it)
        })
        affiliateVM.getDetailList().observe(this,{
            adapter.addMoreData(it)
        })
        affiliateVM.progressBar().observe(this, { visibility ->
            if (visibility != null) {
                if (visibility) {
                   findViewById<LoaderUnify>(R.id.affiliate_progress_bar)?.show()
                    hideView()
                } else {
                    findViewById<LoaderUnify>(R.id.affiliate_progress_bar)?.gone()
                    showView()
                }
            }
        })
    }

    private fun showError(it: Throwable?) {
        findViewById<GlobalError>(R.id.commision_global_error).run {
            when (it) {
                is UnknownHostException, is SocketTimeoutException -> {
                    setType(GlobalError.NO_CONNECTION)
                }
                is IllegalStateException -> {
                    setType(GlobalError.PAGE_FULL)
                }
                else -> {
                    setType(GlobalError.SERVER_ERROR)
                }
            }
            show()
            setActionClickListener {
                hide()
                getData()
            }
        }
    }


    private fun hideView() {
        findViewById<Group>(R.id.details_view).hide()
    }
    private fun showView(){
        findViewById<Group>(R.id.details_view).show()
        findViewById<GlobalError>(R.id.commision_global_error).hide()
    }

    private fun setData(commissionData: AffiliateCommissionDetailsData.GetAffiliateCommissionDetail?) {
        commissionData?.data?.cardDetail?.image?.androidURL?.let { url ->
            findViewById<ImageUnify>(R.id.product_image).setImageUrl(
                url
            )
        }
        findViewById<Typography>(R.id.product_name).text = commissionData?.data?.cardDetail?.cardTitle
        findViewById<Typography>(R.id.product_status).text = commissionData?.data?.cardDetail?.cardPriceFormatted
        findViewById<Typography>(R.id.shop_name).text = commissionData?.data?.cardDetail?.shopName
        findViewById<Typography>(R.id.transaction_date).text = commissionData?.data?.createdAtFormatted

    }

    override fun getLayoutRes(): Int = R.layout.affiliate_transaction_detail_layout

    override fun getViewModelType(): Class<AffiliateTransactionDetailViewModel> {
        return AffiliateTransactionDetailViewModel::class.java
    }
    override fun getVMFactory(): ViewModelProvider.Factory {
        return viewModelProvider
    }

    override fun initInject() {
        getComponent().injectTransactionDetailActivity(this)
    }

    private fun getComponent(): AffiliateComponent =
            DaggerAffiliateComponent
                    .builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .build()

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliateVM = viewModel as AffiliateTransactionDetailViewModel
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun onInfoClick(title: String?, desc: String?) {
        AffiliateHowToPromoteBottomSheet.newInstance(AffiliateHowToPromoteBottomSheet.STATE_PERFORMA_INFO,title,desc).show(supportFragmentManager, "")
    }

}