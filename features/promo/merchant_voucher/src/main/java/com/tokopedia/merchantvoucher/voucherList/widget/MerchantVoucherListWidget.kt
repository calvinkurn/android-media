package com.tokopedia.merchantvoucher.voucherList.widget

import android.annotation.TargetApi
import android.content.Context
import android.content.res.TypedArray
import android.os.Build
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.merchantvoucher.R
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.common.widget.MerchantVoucherView
import com.tokopedia.merchantvoucher.voucherList.adapter.MerchantVoucherAdapterTypeFactory

import java.util.ArrayList

/**
 * [Title]                                [See All]
 * +----------+ +-----+  +----------+ +-----+  +---
 * | VOUCHR_A +-+     |  | VOUCHR_B +-+     |  |
 * | *20Rb     |  USE |  | *20Rb     |  USE |  |
 * |          +-+     |  |          +-+     |  |
 * +----------+ +-----+  +----------+ +-----+  +---
 */
class MerchantVoucherListWidget : FrameLayout, MerchantVoucherView.OnMerchantVoucherViewListener, BaseListAdapter.OnAdapterInteractionListener<MerchantVoucherViewModel> {

    private var titleString: String? = null
    private var titleTextSize: Int = 0
    private var textStyle: Int = 0
    private var titleTextColor: Int = 0

    private var tvTitle: TextView? = null
    private var tvSeeAll: TextView? = null
    var recyclerView: RecyclerView? = null
        private set

    private var adapter: BaseListAdapter<MerchantVoucherViewModel, MerchantVoucherAdapterTypeFactory>? = null

    private var onMerchantVoucherListWidgetListener: OnMerchantVoucherListWidgetListener? = null
    private var rView: View? = null

    override fun isOwner(): Boolean {
        return if (onMerchantVoucherListWidgetListener != null) {
            onMerchantVoucherListWidgetListener!!.isOwner
        } else {
            false
        }
    }

    interface OnMerchantVoucherListWidgetListener {
        val isOwner: Boolean
        fun onMerchantUseVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel, position:Int)
        fun onItemClicked(merchantVoucherViewModel: MerchantVoucherViewModel)
        fun onSeeAllClicked()
    }

    fun setOnMerchantVoucherListWidgetListener(onMerchantVoucherListWidgetListener: OnMerchantVoucherListWidgetListener) {
        this.onMerchantVoucherListWidgetListener = onMerchantVoucherListWidgetListener
    }

    constructor(context: Context) : super(context) {
        applyAttrs(context, null)
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        applyAttrs(context, attrs)
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        applyAttrs(context, attrs)
        init()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        applyAttrs(context, attrs)
        init()
    }

    private fun applyAttrs(context: Context, attributeSet: AttributeSet?) {
        if (attributeSet != null) {
            val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.MerchantVoucherListWidget)
            titleString = typedArray.getString(R.styleable.MerchantVoucherListWidget_mvlwTitleString)
            titleTextSize = typedArray.getDimensionPixelOffset(R.styleable.MerchantVoucherListWidget_mvlwTitleTextSize, 0)
            if (typedArray.hasValue(R.styleable.MerchantVoucherListWidget_android_textStyle)) {
                textStyle = typedArray.getInt(R.styleable.MerchantVoucherListWidget_android_textStyle, 0)
            }
            if (typedArray.hasValue(R.styleable.MerchantVoucherListWidget_android_textColor)) {
                titleTextColor = typedArray.getColor(R.styleable.MerchantVoucherListWidget_android_textColor, 0)
            }
            typedArray.recycle()
        }
    }

    private fun init() {
        rView = LayoutInflater.from(context).inflate(R.layout.merchant_voucher_list_widget,
                this, true)
        recyclerView = rView!!.findViewById(R.id.recycler_view)
        tvTitle = rView!!.findViewById(R.id.tvTitle)
        if (titleTextSize > 0) {
            tvTitle!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize.toFloat())
        }
        tvSeeAll = rView!!.findViewById(R.id.tvSeeAll)
        tvSeeAll!!.setOnClickListener {
            if (onMerchantVoucherListWidgetListener != null) {
                onMerchantVoucherListWidgetListener!!.onSeeAllClicked()
            }
        }
        if (titleTextSize > 0) {
            tvSeeAll!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize.toFloat())
        }
        tvTitle!!.text = titleString
        if (textStyle != 0) {
            tvTitle!!.setTypeface(null, textStyle)
        }
        if (titleTextColor != 0) {
            tvTitle!!.setTextColor(titleTextColor)
        }

        recyclerView!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = BaseListAdapter(
                MerchantVoucherAdapterTypeFactory(this, true),
                this)
        recyclerView!!.adapter = adapter

        rView!!.visibility = View.GONE
        tvSeeAll!!.visibility = View.GONE
    }

    fun setData(merchantVoucherViewModelArrayList: ArrayList<MerchantVoucherViewModel>?) {
        //logic to cater logic since data after refresh will go to index 0 (from api)
        if (merchantVoucherViewModelArrayList != null) {
            if (adapter!!.dataSize == merchantVoucherViewModelArrayList.size) {
                try {
                    recyclerView!!.smoothScrollToPosition(0)
                } catch (e: Exception) {
                    // no op
                }
            }
        }
        adapter!!.clearAllElements()
        if (merchantVoucherViewModelArrayList != null && merchantVoucherViewModelArrayList.size > 0) {
            adapter!!.addElement(merchantVoucherViewModelArrayList)
        }
        val dataSize = adapter!!.dataSize
        if (dataSize > 0) {
            rView!!.visibility = View.VISIBLE
            if (dataSize == 1) {
                tvSeeAll!!.visibility = View.GONE
            } else {
                tvSeeAll!!.visibility = View.VISIBLE
            }
        } else {
            rView!!.visibility = View.GONE
        }
    }

    override fun onMerchantUseVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel) {
        if (onMerchantVoucherListWidgetListener != null) {
            var position: Int = 0
            adapter?.run {
                position = data.indexOf(merchantVoucherViewModel)
                if (position < 0) {
                    position = 0
                }
            }
            onMerchantVoucherListWidgetListener!!.onMerchantUseVoucherClicked(merchantVoucherViewModel, position)
        }
    }

    override fun onItemClicked(o: MerchantVoucherViewModel) {
        if (onMerchantVoucherListWidgetListener != null) {
            onMerchantVoucherListWidgetListener!!.onItemClicked(o)
        }
    }
}
