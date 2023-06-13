package com.tokopedia.common_category.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.common_category.R
import com.tokopedia.common_category.di.BaseCategoryNavComponent
import com.tokopedia.common_category.di.DaggerBaseCategoryNavComponent
import com.tokopedia.common_category.model.bannedCategory.BannedData
import com.tokopedia.common_category.viewmodel.BannedProdNavViewModel
import com.tokopedia.device.info.DeviceInfo
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_banned_product.*
import javax.inject.Inject

private const val QUERY_APP_CLIENT_ID = "?appClientId="
private const val IS_BANNED = 1

abstract class BaseBannedProductFragment : BaseCategorySectionFragment() {
    private var listenerBanned: OnBannedFragmentInteractionListener? = null
    private lateinit var categoryNavComponent: BaseCategoryNavComponent
    protected var bannedData: BannedData? = null

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
        categoryNavComponent = DaggerBaseCategoryNavComponent.builder()
                .baseAppComponent((activity?.applicationContext as BaseMainApplication)
                        .baseAppComponent).build()
        categoryNavComponent.inject(this)
    }

    protected abstract fun addFragmentView()

    protected abstract fun initFragmentView()

    protected abstract fun getDataFromArguments()

    protected abstract fun hideFragmentView()

    protected open fun addBannedProductScreen() {
        hideFragmentView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkIfBanned()
        observeSeamlessLogin()
    }

    private fun checkIfBanned() {
        if (bannedData != null && bannedData?.isBanned == IS_BANNED) {
            addBannedProductScreen()
            showBannedProductScreen(bannedData!!)
        } else {
            addFragmentView()
            initFragmentView()
        }
    }

    protected fun showBannedProductScreen(bannedProductData: BannedData) {
        bannedProductData.let {
            if (bannedProductData.displayButton) {
                category_btn_banned_navigation.show()
                category_btn_banned_navigation.setOnClickListener {
                    onButtonPressed(bannedProductData)
                    val adsId = DeviceInfo.getAdsId(requireContext())
                    var url = Uri.parse(bannedProductData.appRedirection).toString()
                    if (adsId.trim().isNotEmpty()) {
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

    private fun onButtonPressed(bannedProduct: BannedData) {
        listenerBanned?.onButtonClicked(bannedProduct)
    }

    private fun onShowBanned() {
        listenerBanned?.onBannedFragmentAttached()
    }

    override fun onAttach(context: Context) {
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
        fun onButtonClicked(bannedProduct: BannedData)
        fun onBannedFragmentAttached()
    }
}
