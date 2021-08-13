package com.tokopedia.shop.home.view.bottomsheet

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop.R
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created by jegul on 04/11/20
 */
class PlayWidgetSellerActionBottomSheet : BottomSheetUnify() {

    private val adapter = Adapter()

    private lateinit var rvActionList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet()
    }

    private fun setupBottomSheet() {
        setChild(getContentView())
    }

    private fun getContentView(): View {
        val view = View.inflate(requireContext(), R.layout.bottomsheet_play_widget_seller_action_list, null)
        rvActionList = view.findViewById(R.id.rv_action_list)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    fun setActionList(actionList: List<Action>) {
        adapter.setActionList(actionList)
        adapter.notifyDataSetChanged()
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun setupView(view: View) {
        rvActionList.adapter = adapter
        rvActionList.addItemDecoration(ItemDecoration(view.context))
    }

    companion object {
        private const val TAG = "PlayActionListBottomSheet"
    }

    internal class Adapter : RecyclerView.Adapter<ViewHolder>() {

        private val mActionList = mutableListOf<Action>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                    LayoutInflater.from(parent.context).inflate(ViewHolder.layout, parent, false)
            )
        }

        override fun getItemCount(): Int {
            return mActionList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(mActionList[position])
        }

        fun setActionList(actionList: List<Action>) {
            mActionList.clear()
            mActionList.addAll(actionList)
        }
    }

    internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ivIcon: ImageView = itemView.findViewById(R.id.iv_icon)
        val tvSubtitle: TextView = itemView.findViewById(R.id.tv_subtitle)

        fun bind(action: Action) {
            ivIcon.setImageResource(action.iconRes)
            action.tintColor?.let { color ->
                ImageViewCompat.setImageTintList(ivIcon, ColorStateList.valueOf(color))
            }
            tvSubtitle.text = action.subtitle
            itemView.setOnClickListener { action.onClick() }
        }

        companion object {
            val layout = R.layout.item_play_widget_seller_action_list
        }
    }

    internal class ItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

        private val dividerHeight = context.resources.getDimensionPixelOffset(R.dimen.play_widget_seller_action_divider_height)

        private val mPaint = Paint().apply {
            color = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N75)
        }

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val position = parent.getChildAdapterPosition(view)

            if (position != 0) {
                outRect.top = dividerHeight
            } else super.getItemOffsets(outRect, view, parent, state)
        }

        override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            for (index in 0 until parent.childCount) {
                val child = parent.getChildAt(index)

                if (index != 0) {
                    val viewHolder = parent.getChildViewHolder(child)

                    if (viewHolder is ViewHolder) {
                        val start = viewHolder.tvSubtitle.left

                        c.drawRect(
                                Rect(start, child.top - dividerHeight, parent.width, child.top),
                                mPaint
                        )
                    }
                }
            }
        }


    }

    data class Action(
            @DrawableRes val iconRes: Int,
            @ColorInt val tintColor: Int?,
            val subtitle: String,
            val onClick: () -> Unit
    )
}