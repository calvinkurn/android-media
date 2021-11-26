package com.tokopedia.affiliate.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.model.response.AffiliateCommissionDetailsData
import com.tokopedia.affiliate.viewmodel.AffiliateTransactionDetailViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelActivity
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

class AffiliateTransactionDetailActivity : BaseViewModelActivity<AffiliateTransactionDetailViewModel>() {

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
        initObserver()
        intent?.getStringExtra(PARAM_TRANSACTION)?.let { transactionID ->
            affiliateVM.affiliateCommission(transactionID)
        }
    }

    private fun initObserver() {
        affiliateVM.getErrorMessage().observe(this , {

        })
        affiliateVM.getCommissionData().observe(this,{
            setData(it)
        })
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
        setCommissionDetails(commissionData?.data?.detail)

    }

    private fun setCommissionDetails(detail: List<AffiliateCommissionDetailsData.GetAffiliateCommissionDetail.Data.Detail?>?) {
        if(detail?.isNotEmpty() == true)
        {
            detail.getOrNull(0)?.let {
                findViewById<Typography>(R.id.commission_details).text = it.detailTitle
            }
            detail.getOrNull(1)?.let {
                findViewById<Typography>(R.id.transaction_total_price_header).text = it.detailTitle
                findViewById<Typography>(R.id.transaction_total_price).text = it.detailDescription
            }
            detail.getOrNull(2)?.let {
                findViewById<Typography>(R.id.transaction_komisi_header).text = it.detailTitle
                findViewById<Typography>(R.id.transaction_komisi).text = it.detailDescription
            }
            detail.getOrNull(3)?.let {
                findViewById<Typography>(R.id.transaction_total_received_header).text = it.detailTitle
                findViewById<Typography>(R.id.transaction_total_received).text = it.detailDescription
            }
        }
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

}