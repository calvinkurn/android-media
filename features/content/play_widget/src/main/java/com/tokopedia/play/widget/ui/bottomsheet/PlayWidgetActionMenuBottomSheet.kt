package com.tokopedia.play.widget.ui.bottomsheet

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
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.databinding.BottomsheetPlayWidgetSellerActionListBinding
import com.tokopedia.play.widget.databinding.ItemPlayWidgetSellerActionListBinding
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by jegul on 04/11/20
 */
class PlayWidgetActionMenuBottomSheet : BottomSheetUnify() {

    private val adapter = Adapter()

    private var rvActionList: RecyclerView? = null

    private var viewBinding by autoClearedNullable<BottomsheetPlayWidgetSellerActionListBinding>()

    private var mListener: Listener? = null

    private var mChannel: PlayWidgetChannelUiModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet()
        initView()
    }

    private fun initView() {
        rvActionList = viewBinding?.rvActionList
    }

    private fun setupBottomSheet() {
        viewBinding = BottomsheetPlayWidgetSellerActionListBinding.inflate(LayoutInflater.from(context))
        setChild(viewBinding?.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setupActionMenu()
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun setChannel(channel: PlayWidgetChannelUiModel) {
        mChannel = channel
    }

    fun show(fragmentManager: FragmentManager) {
        if(!isAdded) showNow(fragmentManager, TAG)
    }

    private fun setupView(view: View) {
        rvActionList?.adapter = adapter
        rvActionList?.addItemDecoration(ItemDecoration(view.context))
    }

    private fun setupActionMenu() {
        mChannel?.let { channel ->
            val actionList = mutableListOf<Action>()
            if (channel.share.isShow) {
                actionList.add(
                    Action(
                        com.tokopedia.resources.common.R.drawable.ic_system_action_share_grey_24,
                        MethodChecker.getColor(
                            requireContext(),
                            com.tokopedia.unifyprinciples.R.color.Unify_N400
                        ),
                        getString(R.string.play_widget_action_menu_copy_link)
                    ) {
                        mListener?.onClickShare(channel)
                        dismiss()
                    }
                )
            }
            if (channel.performanceSummaryLink.isNotBlank() && channel.performanceSummaryLink.isNotEmpty()) {
                actionList.add(
                    Action(
                        R.drawable.ic_play_widget_sgc_performance,
                        MethodChecker.getColor(
                            requireContext(),
                            com.tokopedia.unifyprinciples.R.color.Unify_N400
                        ),
                        getString(R.string.play_widget_action_menu_see_performance)
                    ) {
                        mListener?.onClickSeePerformance(channel)
                        dismiss()
                    }
                )
            }
            actionList.add(
                Action(
                    com.tokopedia.resources.common.R.drawable.ic_system_action_delete_black_24,
                    MethodChecker.getColor(
                        requireContext(),
                        com.tokopedia.unifyprinciples.R.color.Unify_N400
                    ),
                    getString(R.string.play_widget_action_menu_delete_video)
                ) {
                    mListener?.onClickDeleteVideo(channel)
                    dismiss()
                }
            )

            adapter.setActionList(actionList)
            adapter.notifyDataSetChanged()
        }
    }

    companion object {
        private const val TAG = "PlayActionListBottomSheet"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): PlayWidgetActionMenuBottomSheet {
            val existing = fragmentManager.findFragmentByTag(TAG) as? PlayWidgetActionMenuBottomSheet
            if (existing != null) return existing

            return fragmentManager.fragmentFactory.instantiate(
                classLoader,
                PlayWidgetActionMenuBottomSheet::class.java.name
            ) as PlayWidgetActionMenuBottomSheet
        }
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

        private val viewBindingViewHolder: ItemPlayWidgetSellerActionListBinding? by viewBinding()
        private val ivIcon: ImageView? = viewBindingViewHolder?.ivIcon
        val tvSubtitle: TextView? = viewBindingViewHolder?.tvSubtitle

        fun bind(action: Action) {
            ivIcon?.setImageResource(action.iconRes)
            action.tintColor?.let { color ->
                ivIcon?.let { ImageViewCompat.setImageTintList(it, ColorStateList.valueOf(color)) }
            }
            tvSubtitle?.text = action.subtitle
            itemView.setOnClickListener { action.onClick() }
        }

        companion object {
            val layout = R.layout.item_play_widget_seller_action_list
        }
    }

    internal class ItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

        private val dividerHeight = context.resources.getDimensionPixelOffset(R.dimen.play_widget_action_divider_height)

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
                        val start = viewHolder.tvSubtitle?.left.orZero()

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

    interface Listener {
        fun onClickShare(channel: PlayWidgetChannelUiModel)
        fun onClickSeePerformance(channel: PlayWidgetChannelUiModel)
        fun onClickDeleteVideo(channel: PlayWidgetChannelUiModel)
    }
}
