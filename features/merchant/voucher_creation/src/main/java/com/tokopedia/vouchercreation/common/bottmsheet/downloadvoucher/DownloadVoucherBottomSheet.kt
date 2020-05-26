package com.tokopedia.vouchercreation.common.bottmsheet.downloadvoucher

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.voucherlist.view.adapter.DownloadVoucherAdapter
import kotlinx.android.synthetic.main.bottomsheet_mvc_download_voucher.view.*

/**
 * Created By @ilhamsuaib on 28/04/20
 */

class DownloadVoucherBottomSheet(
        private val parent: ViewGroup
) : BottomSheetUnify() {

    private val mAdapter by lazy { DownloadVoucherAdapter() }
    private var onDownloadClick: (List<DownloadVoucherUiModel>) -> Unit = {}

    init {
        val child = LayoutInflater.from(parent.context)
                .inflate(R.layout.bottomsheet_mvc_download_voucher, parent, false)

        setupView(child)
        setTitle(parent.context.getString(R.string.mvc_select_voucher_size))
        setChild(child)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)

    }

    private fun setupView(child: View) = with(child) view@{
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
                outRect.top = view.resources.getDimensionPixelSize(R.dimen.layout_lvl1)
            }
            if (position == mAdapter.itemCount.minus(1)) {
                outRect.bottom = view.resources.getDimensionPixelSize(R.dimen.layout_lvl2)
            }
        }
    }

    fun setOnDownloadClickListener(action: (List<DownloadVoucherUiModel>) -> Unit): DownloadVoucherBottomSheet {
        this.onDownloadClick = action
        return this
    }

    fun show(fm: FragmentManager) {
        mAdapter.clearAllElements()
        mAdapter.addElement(getDownloadItems())
        showNow(fm, DownloadVoucherBottomSheet::class.java.simpleName)
    }

    private fun getDownloadItems(): List<DownloadVoucherUiModel> {
        return listOf(
                DownloadVoucherUiModel(
                        isSelected = true,
                        ratioStr = parent.context.getString(R.string.mvc_ratio_1_1),
                        description = parent.context.getString(R.string.mvc_for_instagram_facebook_post),
                        downloadVoucherType = DownloadVoucherType.Square("https://ecs7.tokopedia.net/img/attachment/2020/4/8/8967394/8967394_227e871b-f414-4d7d-b1be-2db25d516f19"),
                        onImageOpened = ::onImageExpanded
                ),
                DownloadVoucherUiModel(
                        isSelected = true,
                        ratioStr = parent.context.getString(R.string.mvc_ratio_16_9),
                        description = parent.context.getString(R.string.mvc_for_post_instagram_story),
                        downloadVoucherType = DownloadVoucherType.InstaStory("https://ecs7.tokopedia.net/img/merchant-coupon/banner/v3/base_image/ig_story.jpg"),
                        onImageOpened = ::onImageExpanded
                ),
                DownloadVoucherUiModel(
                        isSelected = true,
                        ratioStr = parent.context.getString(R.string.mvc_shop_cover),
                        description = parent.context.getString(R.string.mvc_for_cover_of_your_shop),
                        downloadVoucherType = DownloadVoucherType.Banner("https://ecs7.tokopedia.net/img/attachment/2020/4/8/8967394/8967394_4bbe15b6-1e27-4903-8b3d-f633397f9e33"),
                        onImageOpened = ::onImageExpanded
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
}