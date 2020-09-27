package com.tokopedia.vouchercreation.common.bottmsheet.downloadvoucher

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationTracking
import com.tokopedia.vouchercreation.voucherlist.view.adapter.DownloadVoucherAdapter
import kotlinx.android.synthetic.main.bottomsheet_mvc_download_voucher.view.*

/**
 * Created By @ilhamsuaib on 28/04/20
 */

class DownloadVoucherBottomSheet : BottomSheetUnify() {

    companion object {
        @JvmStatic
        fun createInstance(bannerUrl: String,
                           squareUrl: String,
                           userId: String): DownloadVoucherBottomSheet {
            return DownloadVoucherBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(BANNER_URL, bannerUrl)
                    putString(SQUARE_URL, squareUrl)
                    putString(USER_ID, userId)
                }
                setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
            }
        }

        private const val BANNER_URL = "banner_url"
        private const val SQUARE_URL = "square_url"
        private const val USER_ID = "user_id"

        const val TAG = "DownloadVoucherBottomSheet"
    }

    private val mAdapter by lazy { DownloadVoucherAdapter() }

    private val bannerUrl by lazy {
        arguments?.getString(BANNER_URL).toBlankOrString()
    }

    private val squareUrl by lazy {
        arguments?.getString(SQUARE_URL).toBlankOrString()
    }

    private val userId by lazy {
        arguments?.getString(USER_ID).toBlankOrString()
    }

    private var onDownloadClick: (List<DownloadVoucherUiModel>) -> Unit = {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initBottomSheet(container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    private fun initBottomSheet(container: ViewGroup?) {
        context?.run {
            val child = View.inflate(this, R.layout.bottomsheet_mvc_download_voucher, container)
            setTitle(getString(R.string.mvc_select_voucher_size))
            setChild(child)
        }
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

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            val position = parent.getChildAdapterPosition(view)
            if (position == 0) {
                outRect.top = view.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl1)
            }
            if (position == mAdapter.itemCount.minus(1)) {
                outRect.bottom = view.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2)
            }
        }
    }

    fun setOnDownloadClickListener(action: (List<DownloadVoucherUiModel>) -> Unit): DownloadVoucherBottomSheet {
        this.onDownloadClick = action
        return this
    }

    fun show(fm: FragmentManager) {
        showNow(fm, TAG)
    }

    private fun getDownloadItems(): List<DownloadVoucherUiModel> {
        return listOf(
                DownloadVoucherUiModel(
                        isSelected = true,
                        ratioStr = context?.getString(R.string.mvc_ratio_1_1).toBlankOrString(),
                        description = context?.getString(R.string.mvc_for_instagram_facebook_post).toBlankOrString(),
                        downloadVoucherType = DownloadVoucherType.Square(squareUrl),
                        onImageOpened = ::onImageExpanded,
                        onCheckBoxClicked = ::onCheckBoxClicked,
                        onChevronIconClicked = ::onChevronItemClicked
                )
        )
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

    private fun onCheckBoxClicked(downloadVoucherType: DownloadVoucherType) {
        VoucherCreationTracking.sendVoucherListClickTracking(
                action =
                        when(downloadVoucherType) {
                            is DownloadVoucherType.Square -> VoucherCreationAnalyticConstant.EventAction.Click.CHOOSE_VOUCHER_SIZE_1
                            is DownloadVoucherType.InstaStory -> VoucherCreationAnalyticConstant.EventAction.Click.CHOOSE_VOUCHER_SIZE_2
                            is DownloadVoucherType.Banner -> VoucherCreationAnalyticConstant.EventAction.Click.CHOOSE_VOUCHER_SIZE_3
                        },
                isActive = true,
                userId = userId
        )
    }

    private fun onChevronItemClicked(downloadVoucherType: DownloadVoucherType) {
        VoucherCreationTracking.sendVoucherListClickTracking(
                action =
                        when(downloadVoucherType) {
                            is DownloadVoucherType.Square -> VoucherCreationAnalyticConstant.EventAction.Click.CHOOSE_VOUCHER_SIZE_1_DROPDOWN
                            is DownloadVoucherType.InstaStory -> VoucherCreationAnalyticConstant.EventAction.Click.CHOOSE_VOUCHER_SIZE_2_DROPDOWN
                            is DownloadVoucherType.Banner -> VoucherCreationAnalyticConstant.EventAction.Click.CHOOSE_VOUCHER_SIZE_3_DROPDOWN
                        },
                isActive = true,
                userId = userId
        )
    }

}