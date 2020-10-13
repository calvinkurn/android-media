package com.tokopedia.searchbar.navigation_component

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.searchbar.R
import kotlinx.android.synthetic.main.toolbar_viewholder_icon.view.*


class NavToolbarIconAdapter(private var iconConfig: IconConfig): RecyclerView.Adapter<IconHolder>() {
    companion object {
        const val STATE_THEME_DARK = 0
        const val STATE_THEME_LIGHT = 1
    }
    private var themeState = STATE_THEME_LIGHT
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.toolbar_viewholder_icon, parent, false)
        return IconHolder(view)
    }

    override fun onBindViewHolder(holder: IconHolder, position: Int) {
        val icon = iconConfig.iconList[position]
        val context = holder.itemView.context

        val unwrappedDrawable: Drawable? = ContextCompat.getDrawable(context, icon.imageRes)
        unwrappedDrawable?.let {
            val wrappedDrawable: Drawable = DrawableCompat.wrap(unwrappedDrawable)
            if (themeState == STATE_THEME_DARK) {
                DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(context, R.color.white))
            } else if (themeState == STATE_THEME_LIGHT) {
                DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(context, R.color.Neutral_N700))
            }
            holder.iconImage.setImageDrawable(wrappedDrawable)
        }
        holder.iconImage.setOnClickListener {
            RouteManager.route(context, icon.applink)
        }

        if (icon.badgeCounter.isZero()) {
            holder.iconBadge.visibility = View.GONE
        } else {
            holder.iconBadge.visibility = View.VISIBLE
            holder.iconBadge.text = icon.badgeCounter.toString()
        }
    }

    override fun getItemCount(): Int {
        return iconConfig.iconList.size
    }

    fun resetIcon(iconConfig: IconConfig) {
        this.iconConfig = iconConfig
        notifyDataSetChanged()
    }

    fun setIconCounter(iconId: Int, counter: Int) {
        val selectedIcon = this.iconConfig.iconList.find { it.id == iconId }
        val selectedIconPosition = this.iconConfig.iconList.indexOf(selectedIcon)
        selectedIcon?.badgeCounter = counter
        notifyItemChanged(selectedIconPosition)
    }

    fun setThemeState(newState: Int) {
        this.themeState = newState
        notifyDataSetChanged()
    }
}

class IconHolder(view: View): RecyclerView.ViewHolder(view) {
    val iconImage = view.nav_icon_image
    val iconBadge = view.nav_icon_badge
}