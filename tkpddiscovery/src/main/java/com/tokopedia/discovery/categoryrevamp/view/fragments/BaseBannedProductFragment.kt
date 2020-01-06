package com.tokopedia.discovery.categoryrevamp.view.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.discovery.R
import com.tokopedia.discovery.categoryrevamp.data.bannedCategory.Data
import com.tokopedia.discovery.categoryrevamp.di.CategoryNavComponent
import com.tokopedia.discovery.categoryrevamp.di.DaggerCategoryNavComponent
import com.tokopedia.discovery.categoryrevamp.viewmodel.BannedProdNavViewModel
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_banned_product.*
import javax.inject.Inject

private const val KEY_ADVERTISING_ID = "KEY_ADVERTISINGID"
private const val ADVERTISING_ID = "ADVERTISINGID"
private const val QUERY_APP_CLIENT_ID = "?appClientId="

abstract class BaseBannedProductFragment : BaseCategorySectionFragment() {
    protected var categoryName: String = ""
    private var listenerBanned: OnBannedFragmentInteractionListener? = null
    private lateinit var categoryNavComponent: CategoryNavComponent

    @Inject
    lateinit var baseViewModelFactory: ViewModelProvider.Factory
    private lateinit var bannedProdNavViewModel: BannedProdNavViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        getDataFromArguments()
        super.onCreate(savedInstanceState)
        initInjectForBaseBannedFragment()
        initBannedProductViewModel()
    }

    private fun initInjectForBaseBannedFragment() {
        categoryNavComponent = DaggerCategoryNavComponent.builder()
                .baseAppComponent((activity?.applicationContext as BaseMainApplication)
                        .baseAppComponent).build()
        categoryNavComponent.inject(this)
    }

    private fun observeBannedProduct() {
        bannedProdNavViewModel.getBannedProductLiveData().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    addBannedProductScreen()
                    showBannedProductScreen(it.data)
                }

                is Fail -> {
                    addFragmentView()
                    initFragmentView()
                }
            }
        })
    }

    protected abstract fun addFragmentView()

    protected abstract fun initFragmentView()

    protected abstract fun getDataFromArguments()

    protected abstract fun hideFragmentView()

    protected fun addBannedProductScreen() {
        hideFragmentView()
        view?.findViewById<View>(R.id.layout_banned_screen)?.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeBannedProduct()
        observeSeamlessLogin()
    }

    protected fun showBannedProductScreen(bannedProductData: Data) {
        bannedProductData.let {
            if (bannedProductData.displayButton) {
                category_btn_banned_navigation.show()
                category_btn_banned_navigation.setOnClickListener {
                    onButtonPressed(bannedProductData)
                    val localCacheHandler = LocalCacheHandler(activity, ADVERTISING_ID)
                    val adsId = localCacheHandler.getString(KEY_ADVERTISING_ID)
                    var url = Uri.parse(bannedProductData.appRedirection).toString()
                    if (adsId != null && adsId.trim().isNotEmpty()) {
                        url = url.plus(QUERY_APP_CLIENT_ID + adsId)
                        bannedProdNavViewModel.openBrowserSeamlessly(url)
                    }
                }
            }
            onShowBanned()
            txt_header.text = bannedProductData.bannedMsgHeader
            txt_sub_header.text = bannedProductData.bannedMessage
        }
    }

    private fun initBannedProductViewModel() {
        val viewModelProvider = ViewModelProviders.of(this, baseViewModelFactory)
        bannedProdNavViewModel = viewModelProvider.get(BannedProdNavViewModel::class.java)
        lifecycle.addObserver(bannedProdNavViewModel)
        bannedProdNavViewModel.categoryName = getDepartMentId()
    }

    private fun observeSeamlessLogin() {
        bannedProdNavViewModel.getSeamlessLoginLiveData().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    openUrlSeamlessly(it.data)
                }

                is Fail -> {
                    onSeamlessError()
                }
            }
        })
    }

    private fun openUrlSeamlessly(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    private fun onSeamlessError() {
        txt_header.text = getString(R.string.category_server_error_header)
        txt_sub_header.text = getString(R.string.category_try_again)
    }

    private fun onButtonPressed(bannedProduct: Data) {
        listenerBanned?.onButtonClicked(bannedProduct)
    }

    private fun onShowBanned() {
        listenerBanned?.onBannedFragmentAttached()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnBannedFragmentInteractionListener) {
            listenerBanned = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listenerBanned = null
    }

    interface OnBannedFragmentInteractionListener {
        fun onButtonClicked(bannedProduct: Data)
        fun onBannedFragmentAttached()
    }
}
