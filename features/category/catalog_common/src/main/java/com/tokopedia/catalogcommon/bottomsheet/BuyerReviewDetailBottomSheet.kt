package com.tokopedia.catalogcommon.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.adapter.ItemProductImageReviewAdapter
import com.tokopedia.catalogcommon.uimodel.BuyerReviewUiModel
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.media.loader.JvmMediaLoader
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class BuyerReviewDetailBottomSheet : BottomSheetUnify() {

    companion object {
        const val TAG = "Tag BuyerReviewDetailBottomSheet"
        const val SHOP_NAME = "shop_name"
        const val SHOP_ICON = "shop_icon"
        const val REVIEWER_AVATAR = "reviewer_avatar"
        const val REVIEWER_NAME = "reviewer_name"
        const val REVIEWER_STATUS = "reviewer_status"

        const val TOTAL_REVIEW = "total_review"
        const val TOTAL_HELP = "total_help"
        const val RATING = "rating"
        const val VARIANT_TYPE = "variant_type"
        const val TIMESTAMP = "timestamp"
        const val REVIEW_DESC = "review_desc"
        const val REVIEW_IMAGES = "review_images"

        const val IMG_ID = "img_id"
        const val IMG_URL = "img_url"

        fun show(
            manager: FragmentManager?,
            reviewData: BuyerReviewUiModel.ItemBuyerReviewUiModel,
            listener: (position: Int) -> Unit = {}
        ) {
            val itemBundle = ArrayList<Bundle>()
            reviewData.images.forEachIndexed { index, imgReview ->
                itemBundle.add(
                    Bundle().apply {
                        putString(IMG_ID, imgReview.id)
                        putString(IMG_URL, imgReview.imgUrl)
                    }
                )
            }
            BuyerReviewDetailBottomSheet().apply {
                setImageClickListener(listener)
                arguments = Bundle().apply {
                    putString(SHOP_ICON, reviewData.shopIcon)
                    putString(SHOP_NAME, reviewData.shopName)
                    putString(REVIEWER_AVATAR, reviewData.avatar)
                    putString(REVIEWER_NAME, reviewData.reviewerName)
                    putString(REVIEWER_STATUS, reviewData.reviewerStatus)
                    putString(VARIANT_TYPE, reviewData.variantName)
                    putInt(TOTAL_REVIEW, reviewData.totalCompleteReview.orZero())
                    putInt(TOTAL_HELP, reviewData.totalHelpedPeople.orZero())
                    putString(TIMESTAMP, reviewData.datetime)
                    putFloat(RATING, reviewData.rating)
                    putString(REVIEW_DESC, reviewData.description)
                    putParcelableArrayList(REVIEW_IMAGES, itemBundle)
                }
            }.show(manager ?: return, TAG)
        }
    }

    private var txtShopName: Typography? = null
    private var imgAvatar: ImageUnify? = null
    private var imgShopIcon: ImageUnify? = null
    private var txtReviewerName: Typography? = null
    private var txtSeparatorName: Typography? = null
    private var txtReviewerStatus: Typography? = null
    private var txtTotalReview: Typography? = null
    private var txtSeparatorTotalReview: Typography? = null
    private var txtTotalHelp: Typography? = null
    private var rating: RatingBar? = null
    private var txtSeparatorVariant: Typography? = null
    private var txtVariantName: Typography? = null
    private var txtSeparatorTimestamp: Typography? = null
    private var txtTimestamp: Typography? = null
    private var txtReviewDescription: Typography? = null
    private var rvImageProducts: RecyclerView? = null
    private var imageClickListener: (position: Int) -> Unit = {}

    init {
        setCloseClickListener { dismiss() }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.apply {
            setTitle(getString(R.string.buyer_review_detail_bottomsheet_title))
            imgShopIcon?.loadImage(getString(SHOP_ICON).orEmpty())
            txtShopName?.text = getString(SHOP_NAME)

            imgAvatar?.let {
                JvmMediaLoader.loadImageFitCenter(it, getString(REVIEWER_AVATAR).orEmpty())
            }
            txtReviewerName?.text = getString(REVIEWER_NAME)
            val reviewerStatus = getString(REVIEWER_STATUS).orEmpty()
            if (reviewerStatus.isNotEmpty()) {
                txtSeparatorName?.visibility = View.VISIBLE
                txtReviewerStatus?.visibility = View.VISIBLE
                txtReviewerStatus?.text = reviewerStatus
            }

            val totalReview = getInt(TOTAL_REVIEW)
            if (!totalReview.isZero()) {
                txtTotalReview?.text = "$totalReview ulasan lengkap"
                txtTotalReview?.visibility = View.VISIBLE
            }

            val totalHelp = getInt(TOTAL_HELP)
            if (!totalHelp.isZero()) {
                txtSeparatorTotalReview?.visibility = View.VISIBLE
                txtTotalHelp?.visibility = View.VISIBLE
                txtTotalHelp?.text = "$totalHelp terbantu"
            }

            rating?.rating = getFloat(RATING)

            val variantName = getString(VARIANT_TYPE).orEmpty()
            if (variantName.isNotEmpty()) {
                txtSeparatorVariant?.visibility = View.VISIBLE
                txtVariantName?.visibility = View.VISIBLE
                txtVariantName?.text = variantName
            }

            val timestamp = getString(TIMESTAMP).orEmpty()
            if (timestamp.isNotEmpty()) {
                txtSeparatorTimestamp?.visibility = View.VISIBLE
                txtTimestamp?.visibility = View.VISIBLE
                txtTimestamp?.text = timestamp
            }

            txtReviewDescription?.text = getString(REVIEW_DESC)

            val imgBundleList = getParcelableArrayList<Bundle>(REVIEW_IMAGES)
            imgBundleList?.let {
                val imgList: List<BuyerReviewUiModel.ImgReview> = it.map { bundle ->
                    val imgId = bundle.getString(IMG_ID).orEmpty()
                    val imgUrl = bundle.getString(IMG_URL).orEmpty()
                    BuyerReviewUiModel.ImgReview(imgId, imgUrl)
                }

                rvImageProducts?.apply {
                    adapter = ItemProductImageReviewAdapter(imgList, imageClickListener)
                    layoutManager = LinearLayoutManager(
                        context,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                }
            }
        }
    }

    private fun initChildLayout() {
        overlayClickDismiss = true
        clearContentPadding = true
        val contentView: View? = View.inflate(context, R.layout.buyer_review_detail_bottomsheet, null)
        contentView?.apply {
            txtShopName = findViewById(R.id.card_br_shop_name)
            imgAvatar = findViewById(R.id.card_br_reviewer_avatar)
            imgShopIcon = findViewById(R.id.card_br_shop_icon)
            txtReviewerName = findViewById(R.id.card_br_reviewer_name)
            txtTotalReview = findViewById(R.id.card_br_total_review)
            txtSeparatorTotalReview = findViewById(R.id.card_br_separator_total_help)
            txtTotalHelp = findViewById(R.id.card_br_review_total_help)
            txtSeparatorName = findViewById(R.id.card_br_separator_status)
            txtReviewerStatus = findViewById(R.id.card_br_reviewer_status)

            rating = findViewById(R.id.card_br_product_rating)
            txtSeparatorVariant = findViewById(R.id.card_br_separator_rating)
            txtVariantName = findViewById(R.id.card_br_variant)
            txtSeparatorTimestamp = findViewById(R.id.card_br_separator_timestamp)
            txtTimestamp = findViewById(R.id.card_br_datetime)
            txtReviewDescription = findViewById(R.id.card_br_product_review)

            rvImageProducts = findViewById(R.id.rv_img_review)
        }

        setChild(contentView)
    }

    fun setImageClickListener(listener: (position: Int) -> Unit) {
        imageClickListener = listener
    }
}
