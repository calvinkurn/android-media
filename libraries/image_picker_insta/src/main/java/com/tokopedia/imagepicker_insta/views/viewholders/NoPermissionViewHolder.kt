package com.tokopedia.imagepicker_insta.views.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.imagepicker_insta.R
import com.tokopedia.unifyprinciples.Typography

class NoPermissionViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    companion object {
        fun getInstance(parent: ViewGroup): NoPermissionViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.imagepicker_insta_no_permission_item_view, parent, false)
            return NoPermissionViewHolder(v)
        }
    }

    val icon: AppCompatImageView = itemView.findViewById(R.id.image_perm)
    val tv: Typography = itemView.findViewById(R.id.tv_perm_vh)
    val btn: Typography = itemView.findViewById(R.id.btn_perm)
    val hasPermissionIcon: AppCompatImageView = itemView.findViewById(R.id.image_check)
    val divider: View = itemView.findViewById(R.id.divider)

    fun setData(@DrawableRes iconRes: Int, text: String, hasPermission: Boolean) {
        tv.text = text
        icon.setImageResource(iconRes)
        if (hasPermission) {
            hasPermissionIcon.visibility = View.VISIBLE
            btn.visibility = View.GONE
        } else {
            hasPermissionIcon.visibility = View.GONE
            btn.visibility = View.VISIBLE
        }
    }
}