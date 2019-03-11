package com.tokopedia.groupchat.chatroom.view.adapter.chatroom

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.widget.ViewTooltip
import com.tokopedia.groupchat.R
import com.tokopedia.groupchat.room.view.listener.PlayContract
import com.tokopedia.groupchat.room.view.viewmodel.DynamicButtonsViewModel

/**
 * @author by StevenFredian on 05/06/18.
 */

class DynamicButtonAdapter(
        private val activity: Activity,
        private val listener: PlayContract.View
) : RecyclerView.Adapter<DynamicButtonAdapter.ViewHolder>() {

    private val list: ArrayList<DynamicButtonsViewModel.Button> = arrayListOf()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var icon: ImageView = view.findViewById(R.id.icon)
        var notification: View = view.findViewById(R.id.notification)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.dynamic_icon_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var element = list[position]
        ImageHandler.loadImage(activity, holder.icon, element.imageUrl, R.drawable.ic_play_dynamic_icon)
        holder.icon.setOnClickListener {
            listener.onDynamicIconClicked(element)
            element.hasNotification = false
            notifyItemChanged(position)
        }

        if (element.hasNotification) {
            holder.notification.visibility = View.VISIBLE
        } else {
            holder.notification.visibility = View.INVISIBLE
        }

        if (element.tooltip.isNotBlank()) {
            ViewTooltip.on(activity, holder.icon)
                    .autoHide(true, 5000)
                    .corner(30)
                    .clickToHide(false)
                    .color(MethodChecker.getColor(activity, R.color.white))
                    .textColor(MethodChecker.getColor(activity, R.color.black_70))
                    .position(ViewTooltip.Position.TOP)
                    .text(element.tooltip)
                    .textSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                    .show()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getList(): List<DynamicButtonsViewModel.Button> {
        return list
    }

    fun setList(list: ArrayList<DynamicButtonsViewModel.Button>) {
        list.let {
            this.list.clear()
            this.list.addAll(it)
            notifyDataSetChanged()
        }
    }
}