package com.tokopedia.discovery.categoryrevamp.view.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

private const val BANNED_PRODUCT = "banned_product"
private const val KEY_ADVERTISING_ID = "KEY_ADVERTISINGID"
private const val ADVERTISING_ID = "ADVERTISINGID"
private const val QUERY_APP_CLIENT_ID = "?appClientId="

class BannedProductFragment : Fragment() {
    private var bannedProduct: Data? = null
    private var listenerBanned: OnBannedFragmentInteractionListener? = null
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var bannedProdNavViewModel: BannedProdNavViewModel
    private lateinit var categoryNavComponent: CategoryNavComponent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bannedProduct = it.getParcelable(BANNED_PRODUCT)
        }
        initInjector()
    }

    private fun initInjector() {
        categoryNavComponent = DaggerCategoryNavComponent.builder()
                .baseAppComponent((activity?.applicationContext as BaseMainApplication)
                        .baseAppComponent).build()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_banned_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryNavComponent.inject(this)
        initViewModel()
        observeSeamlessLogin()
        showBannedProductScreen()
    }

    private fun showBannedProductScreen() {
        bannedProduct.let {
            if (bannedProduct?.displayButton == true) {
                category_btn_banned_navigation.show()
                category_btn_banned_navigation.setOnClickListener {
                    onButtonPressed(bannedProduct!!)
                    val localCacheHandler = LocalCacheHandler(activity, ADVERTISING_ID)
                    val adsId = localCacheHandler.getString(KEY_ADVERTISING_ID)
                    var url = Uri.parse(bannedProduct?.appRedirection).toString()
                    if (adsId != null && adsId.trim().isNotEmpty()) {
                        url = url.plus(QUERY_APP_CLIENT_ID + adsId)
                        bannedProdNavViewModel.openBrowserSeamlessly(url)
                    }
                }
            }
            txt_header.text = bannedProduct?.bannedMsgHeader
            txt_sub_header.text = bannedProduct?.bannedMessage
        }
    }

    private fun initViewModel() {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        bannedProdNavViewModel = viewModelProvider.get(BannedProdNavViewModel::class.java)
    }

    private fun observeSeamlessLogin() {
        bannedProdNavViewModel.mSeamlessLogin.observe(this, Observer {
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
        txt_sub_header.text = getString(R.string.try_again)
    }

    private fun onButtonPressed(bannedProduct: Data) {
        listenerBanned?.onButtonClicked(bannedProduct)
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
        fun onButtonClicked(bannedProduct: Data)
    }

    companion object {
        @JvmStatic
        fun newInstance(bannedProduct: Data) =
                BannedProductFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(BANNED_PRODUCT, bannedProduct)
                    }
                }
    }
}
