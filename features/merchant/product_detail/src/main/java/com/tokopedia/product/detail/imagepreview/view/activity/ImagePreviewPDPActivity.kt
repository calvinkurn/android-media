package com.tokopedia.product.detail.imagepreview.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.imagepreview.data.ImagePreviewTracking
import com.tokopedia.product.detail.imagepreview.di.DaggerImagePreviewPDPComponent
import com.tokopedia.product.detail.imagepreview.view.listener.ImagePreviewPDPView
import com.tokopedia.product.detail.imagepreview.view.viewmodel.ImagePreviewPDPViewModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.activity_image_preview_pdp.*
import kotlinx.android.synthetic.main.item_rates_estimation_service.*
import java.util.*
import javax.inject.Inject
import kotlin.properties.Delegates

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
    private var isWishlisted by Delegates.notNull<Boolean>()

    override fun layoutId(): Int {
        return R.layout.activity_image_preview_pdp
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<Button>(R.id.ivDownload)?.hide()

        val extraData = intent?.extras
        if (extraData != null) {
            productId = extraData.getString(PRODUCT_ID) as String
            isWishlisted = extraData.getBoolean(IS_WISHLISTED, false)
        }

        initInjector()
        updateView()
        initListener()
    }

    private fun initInjector() {
        DaggerImagePreviewPDPComponent.builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    private fun updateView() {
        if (userSession.isLoggedIn) {
            if (isWishlisted) {
                btnAddToWishlist?.text = resources.getString(R.string.image_preview_remove_wishlist)
//                btnAddToWishlist?.buttonType = UnifyButton.Type.TRANSACTION
            } else {
                btnAddToWishlist?.text = resources.getString(R.string.image_preview_add_wishlist)
//                btnAddToWishlist?.buttonType = UnifyButton.Type.MAIN
            }
        } else {
            btnAddToWishlist?.text = resources.getString(R.string.image_preview_add_wishlist)
            btnAddToWishlist?.buttonType = UnifyButton.Type.MAIN
        }
    }

    private fun initListener() {
        btnAddToWishlist?.setOnClickListener {
            if (userSession.isLoggedIn) {
                if (isWishlisted) {
                    removeWishlist()
                } else {
                    addWishlist()
                }
            } else {
                gotoLogin()
                ImagePreviewTracking().onAddWishlistNonLogin()
            }
        }

        findViewById<ImageView>(R.id.ivClose)?.setOnClickListener {
            setResult()
        }
    }

    override fun onBackPressed() {
        setResult()
    }

    private fun setResult() {
        val intent = Intent()
        intent.putExtra(RESPONSE_CODE_IMAGE_RPEVIEW, isWishlisted)
        setResult(Activity.RESULT_OK, intent)
        finish()
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
                onSuccessAddWishlist = {
                    hideLoading()
                    onSuccessAddWishlist()
                    updateView()
                    ImagePreviewTracking().onSuccessAdd()
                },
                onErrorAddWishList = {
                    hideLoading()
                    onErrorAddWishlist(Throwable(it))
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
                    updateView()
                    ImagePreviewTracking().onSuccessRemove()
                },
                onErrorRemoveWishList = {
                    hideLoading()
                    onErrorAddWishlist(Throwable(it))
                }
        )
    }

    override fun showLoadin() {
        progressBar?.show()
    }

    override fun hideLoading() {
        progressBar?.hide()
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
        showMessage(throwable.message.toString())
    }

    override fun showMessage(message: String) {

    }

    companion object {

        private const val KEY_WISHLIST_BUTTON = "image_preview_pdp_wishlist_butotn"

        private const val PRODUCT_ID = "productId"
        private const val IS_WISHLISTED = "isWishlisted"

        private const val REQUEST_CODE_LOGIN = 561
        const val RESPONSE_CODE_IMAGE_RPEVIEW = "responseImagePreview"

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