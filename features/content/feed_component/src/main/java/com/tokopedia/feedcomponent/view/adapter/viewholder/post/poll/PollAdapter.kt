package com.tokopedia.feedcomponent.view.adapter.viewholder.post.poll

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.viewmodel.post.poll.PollContentOptionViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.poll.PollContentViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.item_poll_option.view.*

/**
 * @author by milhamj on 12/12/18.
 */
class PollAdapter(private val contentPosition: Int,
                  private val pollViewModel: PollContentViewModel,
                  private val listener: PollOptionListener)
    : RecyclerView.Adapter<PollAdapter.OptionViewHolder>() {

    private val optionList: MutableList<PollContentOptionViewModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_poll_option, parent, false)
        return OptionViewHolder(view, contentPosition, pollViewModel, listener)
    }

    override fun getItemCount() = optionList.size

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.bind(optionList[position])
    }

    fun setList(list: MutableList<PollContentOptionViewModel>) {
        optionList.clear()
        optionList.addAll(list)
        notifyDataSetChanged()
    }

    class OptionViewHolder(v: View,
                           private val contentPosition: Int,
                           private val pollViewModel: PollContentViewModel,
                           private val listener: PollOptionListener?)
        : RecyclerView.ViewHolder(v) {

        fun bind(element: PollContentOptionViewModel) {
            val context = itemView.context
            if (element.selected == PollContentOptionViewModel.DEFAULT) {
                itemView.shadowLayer.hide()
                itemView.percent.hide()
                itemView.percentLayout.hide()
                itemView.progressBar.progress = 0
                itemView.progressBar.progressDrawable = MethodChecker.getDrawable(context, R.drawable.poll_option_image_default)
            } else {
                itemView.shadowLayer.show()
                itemView.percent.show()
                itemView.percentLayout.show()
                itemView.progressBar.progress = element.percentage
                if (element.selected == PollContentOptionViewModel.SELECTED) {
                    itemView.progressBar.progressDrawable = MethodChecker.getDrawable(context, R.drawable.poll_option_image_selected)
                } else if (element.selected == PollContentOptionViewModel.UNSELECTED) {
                    itemView.progressBar.progressDrawable = MethodChecker.getDrawable(context, R.drawable.poll_option_image_unselected)
                }
            }

            itemView.option.text = element.option
            itemView.percent.text = element.percentage.toString()
            ImageHandler.loadImageWithTarget(
                    itemView.imageView.context,
                    element.imageUrl,
                    object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(arg0: Bitmap, arg1: GlideAnimation<in Bitmap>) {
                            itemView.imageView.setImageBitmap(arg0)
                            itemView.imageView.post {
                                itemView.shadowLayer.layoutParams = RelativeLayout.LayoutParams(
                                        itemView.imageView.height,
                                        itemView.imageView.width)

                                if (element.selected == PollContentOptionViewModel.DEFAULT) {
                                    itemView.shadowLayer.visibility = View.GONE
                                } else {
                                    itemView.shadowLayer.visibility = View.VISIBLE
                                }
                            }

                        }
                    }
            )

            itemView.setOnClickListener {
                listener?.onPollOptionClick(
                        pollViewModel.positionInFeed,
                        contentPosition,
                        adapterPosition + 1,
                        pollViewModel.pollId,
                        element.optionId,
                        pollViewModel.voted,
                        element.redirectLink
                )
            }
        }
    }

    interface PollOptionListener {
        fun onPollOptionClick(positionInFeed: Int, contentPosition: Int, option: Int, pollId: String, optionId: String, isVoted: Boolean, redirectLink: String)
    }
}