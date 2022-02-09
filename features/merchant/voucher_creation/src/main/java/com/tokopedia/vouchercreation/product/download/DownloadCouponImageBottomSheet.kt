package com.tokopedia.vouchercreation.product.download

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationTracking
import kotlinx.android.synthetic.main.bottomsheet_mvc_download_voucher.view.*


class DownloadCouponImageBottomSheet : BottomSheetUnify() {

    companion object {
        private const val BANNER_URL = "banner_url"
        private const val SQUARE_URL = "square_url"
        private const val PORTRAIT_URL = "portrait_url"
        private const val USER_ID = "user_id"

        @JvmStatic
        fun newInstance(
            bannerUrl: String,
            squareUrl: String,
            portraitUrl: String,
            userId: String
        ): DownloadCouponImageBottomSheet {
            return DownloadCouponImageBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(BANNER_URL, bannerUrl)
                    putString(SQUARE_URL, squareUrl)
                    putString(PORTRAIT_URL, portraitUrl)
                    putString(USER_ID, userId)
                }
                setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
            }
        }
    }

    private val mAdapter by lazy { DownloadCouponImageAdapter() }

    private val bannerUrl by lazy {
        arguments?.getString(BANNER_URL).toBlankOrString()
    }

    private val squareUrl by lazy {
        arguments?.getString(SQUARE_URL).toBlankOrString()
    }

    private val portraitUrl by lazy {
        arguments?.getString(PORTRAIT_URL).toBlankOrString()
    }

    private val userId by lazy {
        arguments?.getString(USER_ID).toBlankOrString()
    }

    private var onDownloadClick: (List<CouponImageUiModel>) -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet(container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    private fun setupBottomSheet(container: ViewGroup?) {
        val child =
            View.inflate(requireActivity(), R.layout.bottomsheet_download_coupon_image, container)
        setTitle(getString(R.string.choose_coupon_image_ratio))
        setChild(child)
    }

    private fun setupView(child: View) = with(child) view@{
        mAdapter.addElement(getDownloadItems())
        rvMvcVouchers.run {
            layoutManager = LinearLayoutManager(this@view.context)
            adapter = mAdapter
            addItemDecoration(getItemDecoration())
        }

        btnMvcDownloadVoucher.setOnClickListener {
            onDownloadClick(mAdapter.items.filter { it.isSelected })
            dismiss()
        }
    }

    private fun getItemDecoration() = object : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            val position = parent.getChildAdapterPosition(view)
            if (position == 0) {
                outRect.top =
                    view.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl1)
            }
            if (position == mAdapter.itemCount.minus(1)) {
                outRect.bottom =
                    view.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2)
            }
        }
    }

    fun setOnDownloadClickListener(action: (List<CouponImageUiModel>) -> Unit) {
        this.onDownloadClick = action
    }


    private fun getDownloadItems(): List<CouponImageUiModel> {
        val images = mutableListOf<CouponImageUiModel>()
        if (squareUrl.isNotEmpty()) {
            images.add(
                CouponImageUiModel(
                    isSelected = true,
                    ratioStr = context?.getString(R.string.mvc_ratio_1_1).toBlankOrString(),
                    description = context?.getString(R.string.mvc_for_instagram_facebook_post)
                        .toBlankOrString(),
                    imageType = ImageType.Square(squareUrl),
                    onImageOpened = ::onImageExpanded,
                    onCheckBoxClicked = ::onCheckBoxClicked,
                    onChevronIconClicked = ::onChevronItemClicked
                )
            )
        }
        if (portraitUrl.isNotEmpty()) {
            images.add(
                CouponImageUiModel(
                    isSelected = true,
                    ratioStr = context?.getString(R.string.mvc_ratio_16_9).toBlankOrString(),
                    description = context?.getString(R.string.mvc_for_post_instagram_story)
                        .toBlankOrString(),
                    imageType = ImageType.Portrait(portraitUrl),
                    onImageOpened = ::onImageExpanded,
                    onCheckBoxClicked = ::onCheckBoxClicked,
                    onChevronIconClicked = ::onChevronItemClicked
                )
            )
        }

        if (bannerUrl.isNotEmpty()) {
            images.add(
                CouponImageUiModel(
                    isSelected = true,
                    ratioStr = context?.getString(R.string.banner).toBlankOrString(),
                    description = context?.getString(R.string.banner_image_description)
                        .toBlankOrString(),
                    imageType = ImageType.Square(bannerUrl),
                    onImageOpened = ::onImageExpanded,
                    onCheckBoxClicked = ::onCheckBoxClicked,
                    onChevronIconClicked = ::onChevronItemClicked
                )
            )
        }

        return images
    }

    private fun onImageExpanded(openedIndex: Int) {
        var notifiedIndex = -1
        mAdapter.items.forEachIndexed { index, downloadVoucherUiModel ->
            if (openedIndex != index && downloadVoucherUiModel.isExpanded) {
                downloadVoucherUiModel.isExpanded = false
                notifiedIndex = index
            }
        }
        if (notifiedIndex >= 0) {
            mAdapter.notifyItemChanged(notifiedIndex)
        }
    }

    private fun onCheckBoxClicked(imageType: ImageType) {
        VoucherCreationTracking.sendVoucherListClickTracking(
            action =
            when (imageType) {
                is ImageType.Square -> VoucherCreationAnalyticConstant.EventAction.Click.CHOOSE_VOUCHER_SIZE_1
                is ImageType.Portrait -> VoucherCreationAnalyticConstant.EventAction.Click.CHOOSE_VOUCHER_SIZE_2
                is ImageType.Banner -> VoucherCreationAnalyticConstant.EventAction.Click.CHOOSE_VOUCHER_SIZE_3
            },
            isActive = true,
            userId = userId
        )
    }

    private fun onChevronItemClicked(imageType: ImageType) {
        VoucherCreationTracking.sendVoucherListClickTracking(
            action =
            when (imageType) {
                is ImageType.Square -> VoucherCreationAnalyticConstant.EventAction.Click.CHOOSE_VOUCHER_SIZE_1_DROPDOWN
                is ImageType.Portrait -> VoucherCreationAnalyticConstant.EventAction.Click.CHOOSE_VOUCHER_SIZE_2_DROPDOWN
                is ImageType.Banner -> VoucherCreationAnalyticConstant.EventAction.Click.CHOOSE_VOUCHER_SIZE_3_DROPDOWN
            },
            isActive = true,
            userId = userId
        )
    }

}