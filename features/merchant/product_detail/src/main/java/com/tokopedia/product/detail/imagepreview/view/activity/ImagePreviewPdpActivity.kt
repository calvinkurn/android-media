package com.tokopedia.product.detail.imagepreview.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.component.ticker.TouchViewPager
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.databinding.ActivityImagePreviewPdpBinding
import com.tokopedia.product.detail.imagepreview.data.ImagePreviewTracking
import com.tokopedia.product.detail.imagepreview.di.DaggerImagePreviewPdpComponent
import com.tokopedia.product.detail.imagepreview.view.listener.ImagePreviewPdpView
import com.tokopedia.product.detail.imagepreview.view.viewmodel.ImagePreviewPdpViewModel
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import com.tokopedia.unifycomponents.Toaster.TYPE_NORMAL
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.TOASTER_RED
import com.tokopedia.wishlistcommon.util.WishlistV2RemoteConfigRollenceUtil
import org.xml.sax.ErrorHandler
import java.util.*
import javax.inject.Inject

/**
 * created by rival on 07/11/19
 * image preview with wishlist button
 */

class ImagePreviewPdpActivity : ImagePreviewActivity(), ImagePreviewPdpView {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    val viewModel by lazy { viewModelProvider.get(ImagePreviewPdpViewModel::class.java) }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var imagePreviewTracking: ImagePreviewTracking

    @Inject
    lateinit var remoteConfig: RemoteConfig

    private var productId: String = "0"
    private var isWishlisted: Boolean = false
    private var shopId: String = ""

    private var binding: ActivityImagePreviewPdpBinding? = null

    override fun layoutId(): Int {
        return R.layout.activity_image_preview_pdp
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagePreviewPdpBinding.bind(findViewById(R.id.imagePreviewPdpContainer))

        val extraData = intent?.extras
        if (extraData != null) {
            productId = extraData.getString(PRODUCT_ID) ?: "0"
            isWishlisted = extraData.getBoolean(IS_WISHLISTED, false)
            shopId = extraData.getString(SHOP_ID, "")
        }

        initInjector()
        updateView()
        initListener()
    }

    private fun initInjector() {
        DaggerImagePreviewPdpComponent.builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    private fun updateView() {
        findViewById<Button>(com.tokopedia.imagepreview.R.id.ivDownload)?.hide()

        if (viewModel.isShopOwner(shopId)) {
            binding?.btnAddToWishlist?.hide()
            return
        }

        val isCanShowing = remoteConfig.getBoolean(KEY_WISHLIST_BUTTON, false)
        if (isCanShowing) {
            binding?.btnAddToWishlist?.show()
        } else {
            binding?.btnAddToWishlist?.hide()
        }

        if (userSession.isLoggedIn) {
            if (isWishlisted) {
                binding?.btnAddToWishlist?.text = resources.getString(R.string.image_preview_remove_wishlist)
            } else {
                binding?.btnAddToWishlist?.text = resources.getString(R.string.image_preview_add_wishlist)
            }
        } else {
            binding?.btnAddToWishlist?.text = resources.getString(R.string.image_preview_add_wishlist)
        }
    }

    private fun initListener() {
        binding?.btnAddToWishlist?.setOnClickListener {
            if (userSession.isLoggedIn) {
                var isWishlistUsingV2 = false
                if (WishlistV2RemoteConfigRollenceUtil.isUsingAddRemoveWishlistV2(this)) isWishlistUsingV2 = true
                if (isWishlisted) {
                    if (isWishlistUsingV2) removeWishlistV2()
                    else removeWishlist()
                } else {
                    if (isWishlistUsingV2) addWishlistV2(this)
                    else addWishlist()
                }
            } else {
                gotoLogin()
                imagePreviewTracking.onAddWishlistNonLogin()
            }
        }

        findViewById<TouchViewPager>(com.tokopedia.imagepreview.R.id.viewPager)?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            var lastPosition = 0
            override fun onPageSelected(position: Int) {
                val swipeDirection = if (lastPosition > position) IMAGE_SWIPE_DIRECTION_LEFT else IMAGE_SWIPE_DIRECTION_RIGHT
                imagePreviewTracking.onImageSwipe(productId, swipeDirection, position)
                lastPosition = position
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }
        })

        findViewById<ImageView>(com.tokopedia.imagepreview.R.id.ivClose)?.setOnClickListener {
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
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_LOGIN -> {
                if (userSession.isLoggedIn) {
                    addWishlist()
                    startActivity(RouteManager.getIntent(applicationContext, ApplinkConst.WISHLIST))
                    finish()
                }
            }
        }
    }

    override fun addWishlist() {
        showLoading()
        viewModel.addWishList(
            productId,
            onSuccessAddWishlist = {
                hideLoading()
                onSuccessAddWishlist()
                updateView()
                imagePreviewTracking.onSuccessAdd()
            },
            onErrorAddWishList = {
                hideLoading()
                it?.let {
                    onErrorAddWishlist(Throwable(it))
                }
            }
        )
    }

    override fun addWishlistV2(context: Context) {
        showLoading()
        viewModel.addWishListV2(
                productId,
                object : WishlistV2ActionListener {
                    override fun onErrorAddWishList(throwable: Throwable, productId: String) {
                        val rootView = findViewById<ConstraintLayout>(R.id.imagePreviewPdpContainer)
                        val errorMsg = getString(com.tokopedia.wishlist_common.R.string.on_failed_add_to_wishlist_msg)
                        Toaster.build(rootView, errorMsg, Toaster.LENGTH_SHORT, TYPE_ERROR).show()
                    }

                    override fun onSuccessAddWishlist(
                        result: AddToWishlistV2Response.Data.WishlistAddV2,
                        productId: String
                    ) {
                        val rootView = findViewById<ConstraintLayout>(R.id.imagePreviewPdpContainer)
                        var msg = ""
                        if (result.message.isEmpty()) {
                            if (result.success) getString(com.tokopedia.wishlist_common.R.string.on_success_add_to_wishlist_msg)
                            else getString(com.tokopedia.wishlist_common.R.string.on_failed_add_to_wishlist_msg)
                        } else {
                            msg = result.message
                        }

                        var typeToaster = TYPE_NORMAL
                        if (result.toasterColor == TOASTER_RED || !result.success) typeToaster = TYPE_ERROR

                        var ctaText = getString(com.tokopedia.wishlist_common.R.string.cta_success_add_to_wishlist)
                        if (result.button.text.isNotEmpty()) ctaText = result.button.text

                        Toaster.build(rootView, msg, Toaster.LENGTH_SHORT, typeToaster, ctaText) {
                                RouteManager.route(context, ApplinkConst.NEW_WISHLIST) }.show()
                    }

                    override fun onErrorRemoveWishlist(throwable: Throwable, productId: String) {}

                    override fun onSuccessRemoveWishlist(productId: String) {}
                }
        )
    }

    override fun removeWishlist() {
        showLoading()
        viewModel.removeWishList(
            productId,
            onSuccessRemoveWishlist = {
                hideLoading()
                onSuccessRemoveWishlist()
                updateView()
                imagePreviewTracking.onSuccessRemove()
            },
            onErrorRemoveWishList = {
                hideLoading()
                onErrorRemoveWishlist(Throwable(it))
            }
        )
    }

    override fun removeWishlistV2() {
        showLoading()
        viewModel.removeWishListV2(
                productId,
                onSuccessRemoveWishlist = {
                    hideLoading()
                    onSuccessRemoveWishlist()
                    updateView()
                    imagePreviewTracking.onSuccessRemove()
                },
                onErrorRemoveWishList = {
                    hideLoading()
                    onErrorRemoveWishlist(Throwable(it))
                }
        )
    }

    override fun gotoLogin() {
        startActivityForResult(RouteManager.getIntent(applicationContext, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN)
    }

    override fun showLoading() {
        binding?.progressBar?.show()
    }

    override fun hideLoading() {
        binding?.progressBar?.hide()
    }

    override fun onSuccessAddWishlist() {
        isWishlisted = true
        showMessage(resources.getString(com.tokopedia.wishlist_common.R.string.on_success_add_to_wishlist_msg))
    }

    override fun onSuccessRemoveWishlist() {
        isWishlisted = false
        showMessage(resources.getString(com.tokopedia.wishlist_common.R.string.on_success_remove_from_wishlist_msg))
    }

    override fun onErrorAddWishlist(throwable: Throwable) {
        showErrorMessage(throwable.message.toString())
    }

    override fun onErrorRemoveWishlist(throwable: Throwable) {
        showErrorMessage(throwable.message.toString())
    }

    override fun showMessage(message: String) {
        val rootView = findViewById<ConstraintLayout>(R.id.imagePreviewPdpContainer)
        Toaster.make(rootView, message, Toaster.toasterLength, Toaster.TYPE_NORMAL)
    }

    override fun showErrorMessage(message: String) {
        val rootView = findViewById<ConstraintLayout>(R.id.imagePreviewPdpContainer)
        Toaster.make(rootView, message, Toaster.toasterLength, TYPE_ERROR)
    }

    companion object {

        private const val KEY_WISHLIST_BUTTON = "android_customer_image_preview_wishlist_pdp"

        private const val SHOP_ID = "shopId"
        private const val PRODUCT_ID = "productId"
        private const val IS_WISHLISTED = "isWishlisted"
        private const val IMAGE_SWIPE_DIRECTION_LEFT = "left"
        private const val IMAGE_SWIPE_DIRECTION_RIGHT = "right"

        private const val REQUEST_CODE_LOGIN = 561
        const val RESPONSE_CODE_IMAGE_RPEVIEW = "responseImagePreview"

        @JvmStatic
        @JvmOverloads
        fun createIntent(
                context: Context,
                shopId: String,
                productId: String,
                isWishlisted: Boolean,
                imageUris: ArrayList<String>,
                imageDesc: ArrayList<String>? = null,
                position: Int = 0,
                title: String? = null,
                description: String? = null,
                disableDownload: Boolean = true): Intent {
            val intent = Intent(context, ImagePreviewPdpActivity::class.java)
            val bundle = Bundle()
            bundle.putString(TITLE, title)
            bundle.putString(DESCRIPTION, description)
            bundle.putStringArrayList(IMAGE_URIS, imageUris)
            bundle.putStringArrayList(IMAGE_DESC, imageDesc)
            bundle.putInt(IMG_POSITION, position)
            bundle.putString(SHOP_ID, shopId)
            bundle.putString(PRODUCT_ID, productId)
            bundle.putBoolean(IS_WISHLISTED, isWishlisted)
            bundle.putBoolean(DISABLE_DOWNLOAD, disableDownload)
            intent.putExtras(bundle)
            return intent
        }
    }
}