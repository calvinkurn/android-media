package com.tokopedia.product.detail.imagepreview.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.imagepreview.di.DaggerImagePreviewPDPComponent
import com.tokopedia.product.detail.imagepreview.view.listener.ImagePreviewPDPView
import com.tokopedia.product.detail.imagepreview.view.viewmodel.ImagePreviewPDPViewModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.activity_image_preview_pdp.*
import java.util.*
import javax.inject.Inject

/**
 * created by rival on 07/11/19
 * image preview with wishlist button
 */

class ImagePreviewPDPActivity : ImagePreviewActivity(), ImagePreviewPDPView {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    val viewModel by lazy { viewModelProvider.get(ImagePreviewPDPViewModel::class.java) }

    @Inject
    lateinit var userSession: UserSessionInterface

    private var productId: String = ""
    private var isWishlisted: Boolean = false

    override fun layoutId(): Int {
        return R.layout.activity_image_preview_pdp
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<Button>(R.id.ivDownload)?.hide()

        val extraData = intent?.extras
        if (extraData != null) {
            productId = extraData.getString(PRODUCT_ID) as String
            isWishlisted = extraData.getBoolean(IS_WISHLISTED)
        }

        initInjector()
        initListener()
    }

    private fun initInjector() {
        DaggerImagePreviewPDPComponent.builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    private fun initListener() {
        btnAddToWishlist?.setOnClickListener {
            if (userSession.isLoggedIn) {
                if (isWishlisted) {
                    addWishlist()
                } else {
                    removeWishlist()
                }
            } else {
                gotoLogin()
            }
        }

        findViewById<Button>(R.id.ivDownload)?.setOnClickListener {
            // TODO add result
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when(requestCode) {
                REQUEST_CODE_LOGIN -> {
                    startActivity(RouteManager.getIntent(applicationContext, ApplinkConst.WISHLIST))
                    finish()
                }
            }
        }
    }

    override fun addWishlist() {
        showLoadin()
        viewModel.addWishList(
                productId,
                onErrorAddWishList = {
                    hideLoading()
                    onErrorAddWishlist(Throwable(it))
                },
                onSuccessAddWishlist = {
                    hideLoading()
                    onSuccessAddWishlist()
                }
        )
    }

    override fun removeWishlist() {
        showLoadin()
        viewModel.removeWishList(
                productId,
                onSuccessRemoveWishlist = {
                    hideLoading()
                    onSuccessRemoveWishlist()
                },
                onErrorRemoveWishList = {
                    hideLoading()
                    onErrorAddWishlist(Throwable(it))
                }
        )
    }

    override fun showLoadin() {

    }

    override fun hideLoading() {

    }

    override fun gotoLogin() {
        startActivityForResult(RouteManager.getIntent(applicationContext, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN)
    }

    override fun onSuccessAddWishlist() {
        isWishlisted = true
    }

    override fun onSuccessRemoveWishlist() {
        isWishlisted = false
    }

    override fun onErrorAddWishlist(throwable: Throwable) {
        isWishlisted = false
        showMessage(throwable.message.toString())
    }

    override fun showMessage(message: String) {

    }

    companion object {

        private const val PRODUCT_ID = "productId"
        private const val IS_WISHLISTED = "isWishlisted"

        private const val REQUEST_CODE_LOGIN = 561

        @JvmStatic
        @JvmOverloads
        fun createIntent(
                context: Context,
                productId: String,
                isWishlisted: Boolean,
                imageUris: ArrayList<String>,
                imageDesc: ArrayList<String>? = null,
                position: Int = 0,
                title: String? = null,
                description: String? = null): Intent {
            val intent = Intent(context, ImagePreviewPDPActivity::class.java)
            val bundle = Bundle()
            bundle.putString(TITLE, title)
            bundle.putString(DESCRIPTION, description)
            bundle.putStringArrayList(IMAGE_URIS, imageUris)
            bundle.putStringArrayList(IMAGE_DESC, imageDesc)
            bundle.putInt(IMG_POSITION, position)
            bundle.putString(PRODUCT_ID, productId)
            bundle.putBoolean(IS_WISHLISTED, isWishlisted)
            intent.putExtras(bundle)
            return intent
        }
    }
}