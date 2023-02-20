package com.tokopedia.feedcomponent.view.adapter.viewholder.post.poll

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.viewmodel.post.poll.PollContentOptionModel
import com.tokopedia.feedcomponent.view.viewmodel.post.poll.PollContentModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.item_poll_option.view.*

/**
 * @author by milhamj on 12/12/18.
 */
class PollAdapter(private val contentPosition: Int,
                  private val pollViewModel: PollContentModel,
                  private val listener: PollOptionListener)
    : RecyclerView.Adapter<PollAdapter.OptionViewHolder>() {

    private val optionList: MutableList<PollContentOptionModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_poll_option, parent, false)
        return OptionViewHolder(view, contentPosition, pollViewModel, listener)
    }

    override fun getItemCount() = optionList.size

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.bind(optionList[position])
    }

    fun setList(list: MutableList<PollContentOptionModel>) {
        optionList.clear()
        optionList.addAll(list)
        notifyDataSetChanged()
    }

    class OptionViewHolder(v: View,
                           private val contentPosition: Int,
                           private val pollViewModel: PollContentModel,
                           private val listener: PollOptionListener?)
        : RecyclerView.ViewHolder(v) {

        fun bind(element: PollContentOptionModel) {
            val context = itemView.context
            if (element.selected == PollContentOptionModel.DEFAULT) {
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
                if (element.selected == PollContentOptionModel.SELECTED) {
                    itemView.progressBar.progressDrawable = MethodChecker.getDrawable(context, R.drawable.poll_option_image_selected)
                } else if (element.selected == PollContentOptionModel.UNSELECTED) {
                    itemView.progressBar.progressDrawable = MethodChecker.getDrawable(context, R.drawable.poll_option_image_unselected)
                }
            }

            itemView.option.text = element.option
            itemView.percent.text = element.percentage.toString()
            val target = object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {

                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    itemView.imageView.setImageBitmap(resource)
                    itemView.imageView.post {
                        itemView.shadowLayer.layoutParams = RelativeLayout.LayoutParams(
                                itemView.imageView.height,
                                itemView.imageView.width)

                        if (element.selected == PollContentOptionModel.DEFAULT) {
                            itemView.shadowLayer.visibility = View.GONE
                        } else {
                            itemView.shadowLayer.visibility = View.VISIBLE
                        }
                    }
                }

            }
            ImageHandler.loadImageWithTarget(
                    itemView.imageView.context,
                    element.imageUrl,
                    target
            )

            itemView.setOnClickListener {
                listener?.onPollOptionClick(
                        pollViewModel.positionInFeed,
                        contentPosition,
                        adapterPosition,
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
