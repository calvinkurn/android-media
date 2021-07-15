package com.tokopedia.flight.detail.view.adapter

import android.content.res.Resources
import android.graphics.Typeface
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.flight.detail.view.model.SimpleModel
import com.tokopedia.flight.orderlist.R
import java.util.*

/**
 * Created by furqan on 06/10/21.
 */
class FlightSimpleAdapter : RecyclerView.Adapter<FlightSimpleAdapter.ViewHolder>() {

    private var viewModels: MutableList<SimpleModel>
    private var fontSize = 0f
    private var marginTopDp: Float
    private var marginBottomDp: Float
    private var marginLeftDp: Float
    private var marginRightDp: Float
    private var isArrowVisible: Boolean
    private var isClickable: Boolean
    private var isTitleBold: Boolean
    private var isTitleOnly: Boolean
    private var isTitleHalfView: Boolean
    private var isContentAllignmentLeft: Boolean
    private var titleMaxLines: Int
    private var interactionListener: OnAdapterInteractionListener? = null

    @ColorInt
    private var contentColorValue = 0

    init {
        viewModels = ArrayList()
        isArrowVisible = false
        isClickable = false
        isTitleBold = false
        isTitleOnly = false
        isContentAllignmentLeft = false
        isTitleHalfView = true
        titleMaxLines = 1
        marginTopDp = 0f
        marginBottomDp = 0f
        marginLeftDp = 0f
        marginRightDp = 0f
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_simple_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(viewModels[holder.adapterPosition])
    }

    override fun getItemCount(): Int {
        return viewModels.size
    }

    fun setViewModel(simpleViewModel: SimpleModel) {
        viewModels.add(simpleViewModel)
    }

    fun setViewModels(viewModels: MutableList<SimpleModel>) {
        this.viewModels = viewModels
    }

    fun setDescriptionTextColor(@ColorInt colorInt: Int) {
        contentColorValue = colorInt
    }

    fun setArrowVisible(isArrowVisible: Boolean) {
        this.isArrowVisible = isArrowVisible
    }

    fun setClickable(isClickable: Boolean) {
        this.isClickable = isClickable
    }

    fun setTitleBold(isTitleBold: Boolean) {
        this.isTitleBold = isTitleBold
    }

    fun setTitleOnly(isTitleOnly: Boolean) {
        this.isTitleOnly = isTitleOnly
    }

    fun setTitleHalfView(titleHalfView: Boolean) {
        isTitleHalfView = titleHalfView
    }

    fun setFontSize(fontSize: Float) {
        this.fontSize = fontSize
    }

    fun setInteractionListener(interactionListener: OnAdapterInteractionListener?) {
        this.interactionListener = interactionListener
    }

    fun setContentAllignmentLeft(contentAllignmentLeft: Boolean) {
        isContentAllignmentLeft = contentAllignmentLeft
    }

    fun setMarginTopDp(marginTopDp: Float) {
        this.marginTopDp = marginTopDp
    }

    fun setMarginBottomDp(marginBottomDp: Float) {
        this.marginBottomDp = marginBottomDp
    }

    fun setTitleMaxLines(maxLines: Int) {
        titleMaxLines = maxLines
    }

    fun setMarginLeftDp(marginLeftDp: Float) {
        this.marginLeftDp = marginLeftDp
    }

    fun setMarginRightDp(marginRightDp: Float) {
        this.marginRightDp = marginRightDp
    }

    interface OnAdapterInteractionListener {
        fun onItemClick(adapterPosition: Int, viewModel: SimpleModel)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val titleTextView = itemView.findViewById<View>(R.id.tv_title) as TextView
        private val contentTextView = itemView.findViewById<View>(R.id.tv_content) as TextView
        private val arrowImageView = itemView.findViewById<View>(R.id.iv_arrow) as ImageView
        private val containerLinearLayout = itemView.findViewById<View>(R.id.container) as LinearLayout

        fun bind(viewModel: SimpleModel) {
            val layoutParams = titleTextView.layoutParams as LinearLayout.LayoutParams
            titleTextView.text = viewModel.label
            contentTextView.text = viewModel.description
            contentTextView.visibility = if (isTitleOnly) View.GONE else View.VISIBLE
            arrowImageView.visibility = if (isArrowVisible) View.VISIBLE else View.GONE
            if (contentColorValue != 0) {
                contentTextView.setTextColor(contentColorValue)
            }
            if (isTitleBold) {
                titleTextView.typeface = Typeface.DEFAULT_BOLD
            } else {
                titleTextView.typeface = Typeface.DEFAULT
            }
            if (fontSize != 0f) {
                titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
                contentTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
            }
            titleTextView.maxLines = titleMaxLines
            if (isTitleHalfView) {
                layoutParams.width = 0
                layoutParams.weight = 1f
                titleTextView.layoutParams = layoutParams
            } else {
                layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                layoutParams.weight = 0f
                layoutParams.setMargins(
                        PARAM_EMPTY_MARGIN,
                        PARAM_EMPTY_MARGIN,
                        10,
                        PARAM_EMPTY_MARGIN
                )
                titleTextView.layoutParams = layoutParams
                titleTextView.minWidth = 150
            }
            if (isContentAllignmentLeft) {
                contentTextView.gravity = View.TEXT_ALIGNMENT_TEXT_START
            }
            containerLinearLayout.setOnClickListener {
                if (interactionListener != null) {
                    interactionListener!!.onItemClick(adapterPosition, viewModel)
                }
            }
            if (isClickable) {
                containerLinearLayout.background = MethodChecker.getDrawable(itemView.context, com.tokopedia.abstraction.R.drawable.selectable_background_tokopedia)
            } else {
                containerLinearLayout.background = null
            }
            val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            )
            val resources = itemView.context.resources
            params.setMargins(
                    convertToPixel(resources, marginLeftDp),
                    convertToPixel(resources, marginTopDp),
                    convertToPixel(resources, marginRightDp),
                    convertToPixel(resources, marginBottomDp)
            )
            containerLinearLayout.layoutParams = params
        }

    }

    private fun convertToPixel(resources: Resources, dp: Float): Int {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_PX,
                dp,
                resources.displayMetrics
        ).toInt()
    }

    companion object {
        private const val PARAM_EMPTY_MARGIN = 0
    }
}