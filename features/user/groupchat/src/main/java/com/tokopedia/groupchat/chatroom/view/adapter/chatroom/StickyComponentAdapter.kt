package com.tokopedia.groupchat.chatroom.view.adapter.chatroom

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Typeface
import androidx.recyclerview.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.groupchat.R
import com.tokopedia.groupchat.chatroom.domain.pojo.AttributeStickyComponentData
import com.tokopedia.groupchat.room.view.viewmodel.pinned.StickyComponentViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import java.util.concurrent.TimeUnit

/**
 * @author : Steven 22/05/19
 */

class StickyComponentAdapter(var eventClickStickyComponent: (item: StickyComponentViewModel) -> Unit
                             , var eventViewStickyComponent: (item: StickyComponentViewModel) -> Unit
                             , var eventGoToAtc: (item: AttributeStickyComponentData, productName: String, productPrice: String) -> Unit)
    : RecyclerView.Adapter<StickyComponentAdapter.StickyComponentViewHolder>() {

    private var list: ArrayList<StickyComponentViewModel> = arrayListOf()

    class StickyComponentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title = itemView.findViewById<TextView>(R.id.tv_title)
        val subtitle = itemView.findViewById<TextView>(R.id.tv_subtitle)
        val image = itemView.findViewById<ImageView>(R.id.iv_image)
        val byMeIcon = itemView.findViewById<ImageView>(R.id.iv_byme)
        val atcStickyPlay = itemView.findViewById<ImageView>(R.id.atc_sticky_play)


        fun bindElement(
                stickyComponentViewModel: StickyComponentViewModel,
                dismissItem: () -> Unit,
                eventClickStickyComponent: (item: StickyComponentViewModel) -> Unit,
                eventViewStickyComponent: (item: StickyComponentViewModel) -> Unit,
                eventGoToAtc : (item: AttributeStickyComponentData, productName: String, productPrice: String) -> Unit
        ) {
            byMeIcon.hide()

            ImageHandler.LoadImage(image, stickyComponentViewModel.imageUrl)

            title.text = getTitle(stickyComponentViewModel.title)
            title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            title.setTypeface(null, Typeface.NORMAL)
            title.setTextColor(MethodChecker.getColor(title.context, com.tokopedia.design.R.color.black_70))

            subtitle.text = MethodChecker.fromHtml(stickyComponentViewModel.subtitle)
            subtitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            subtitle.setTypeface(null, Typeface.BOLD)
            subtitle.setTextColor(MethodChecker.getColor(title.context, com.tokopedia.design.R.color.orange_red))
            atcStickyPlay.hide()


            itemView.animate().setDuration(200)
                    .alpha(1f)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            eventViewStickyComponent.invoke(stickyComponentViewModel)
                        }
                    })
            val hideStickyComponent = Action1<Long> {
                itemView.animate().setDuration(200)
                        .alpha(0f)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                itemView.hide()
                                dismissItem.invoke()
                            }
                        })
            }
            if (stickyComponentViewModel.stickyTime != 0) {
                Observable.timer(stickyComponentViewModel.stickyTime.toLong(), TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(hideStickyComponent, Action1 { it.printStackTrace() })
            }

            itemView.setOnClickListener {
                eventClickStickyComponent.invoke(stickyComponentViewModel)
            }

            setSpecificView(stickyComponentViewModel, eventGoToAtc)
        }

        private fun getTitle(title: String): CharSequence? {
            val spanned = MethodChecker.fromHtml(title)
            var editedTitle = ""
            editedTitle = if(spanned.length > 15) {
                spanned.take(12).toString().plus("...")
            } else {
                spanned.toString()
            }
            return editedTitle
        }

        private fun setSpecificView(
                stickyComponentViewModel: StickyComponentViewModel,
                eventGoToAtc: (item: AttributeStickyComponentData, productName: String, productPrice: String) -> Unit
        ) {
            when (stickyComponentViewModel.componentType.toLowerCase()) {
                StickyComponentViewModel.TYPE_PRODUCT -> {
                    atcStickyPlay.show()
                    atcStickyPlay.setOnClickListener {
                        val attribute = Gson().fromJson(
                                stickyComponentViewModel.attributeData,
                                AttributeStickyComponentData::class.java
                        )

                        eventGoToAtc.invoke(attribute, stickyComponentViewModel.title, stickyComponentViewModel.subtitle)
                    }
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): StickyComponentViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_sticky_profile, parent, false)

        return StickyComponentViewHolder(view)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: StickyComponentViewHolder, position: Int) {
        holder.bindElement(
                list[position],
                dismissItem(position),
                eventClickStickyComponent,
                eventViewStickyComponent,
                eventGoToAtc
        )
    }

    override fun onViewRecycled(holder: StickyComponentViewHolder) {
        super.onViewRecycled(holder)
        ImageHandler.clearImage(holder.image)
    }

    fun setList(list: List<StickyComponentViewModel>) {
        this.list = ArrayList(list)
        notifyDataSetChanged()
    }

    fun clearList() {
        this.list.clear()
        notifyDataSetChanged()
    }

    private fun dismissItem(position: Int): () -> Unit {
        return {
            if(list.size > position) {
                list.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }
}