package com.tokopedia.feedcomponent.view.adapter.viewholder.post.poll

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.image.SquareImageView
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.viewmodel.post.poll.PollContentModel
import com.tokopedia.feedcomponent.view.viewmodel.post.poll.PollContentOptionModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.getBitmapImageUrl

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

    @SuppressLint("NotifyDataSetChanged")
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

        private val shadowLayer: RelativeLayout = itemView.findViewById(R.id.shadowLayer)
        private val percent: TextView = itemView.findViewById(R.id.percent)
        private val percentLayout: LinearLayout = itemView.findViewById(R.id.percentLayout)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
        private val tvOption: TextView = itemView.findViewById(R.id.option)
        private val imageView: SquareImageView = itemView.findViewById(R.id.imageView)

        fun bind(element: PollContentOptionModel) {
            val context = itemView.context
            if (element.selected == PollContentOptionModel.DEFAULT) {
                shadowLayer.hide()
                percent.hide()
                percentLayout.hide()
                progressBar.progress = 0
                progressBar.progressDrawable = MethodChecker.getDrawable(context, R.drawable.poll_option_image_default)
            } else {
                shadowLayer.show()
                percent.show()
                percentLayout.show()
                progressBar.progress = element.percentage
                if (element.selected == PollContentOptionModel.SELECTED) {
                    progressBar.progressDrawable = MethodChecker.getDrawable(context, R.drawable.poll_option_image_selected)
                } else if (element.selected == PollContentOptionModel.UNSELECTED) {
                    progressBar.progressDrawable = MethodChecker.getDrawable(context, R.drawable.poll_option_image_unselected)
                }
            }

            tvOption.text = element.option
            percent.text = element.percentage.toString()

            element.imageUrl.getBitmapImageUrl(imageView.context) {
                imageView.setImageBitmap(it)
                imageView.post {
                    shadowLayer.layoutParams = RelativeLayout.LayoutParams(
                        imageView.height,
                        imageView.width)

                    if (element.selected == PollContentOptionModel.DEFAULT) {
                        shadowLayer.gone()
                    } else {
                        shadowLayer.visible()
                    }
                }
            }

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
