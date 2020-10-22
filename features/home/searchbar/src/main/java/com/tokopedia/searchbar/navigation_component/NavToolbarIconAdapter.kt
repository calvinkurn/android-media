package com.tokopedia.searchbar.navigation_component

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.searchbar.R
import com.tokopedia.searchbar.navigation_component.icons.IconConfig
import com.tokopedia.searchbar.navigation_component.icons.IconToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconToolbar.Companion.TYPE_LOTTIE
import kotlinx.android.synthetic.main.toolbar_viewholder_icon.view.*
import kotlinx.android.synthetic.main.toolbar_viewholder_icon_lottie.view.*

class NavToolbarIconAdapter(private var iconConfig: IconConfig): RecyclerView.Adapter<IconHolder>() {
    companion object {
        const val STATE_THEME_DARK = 0
        const val STATE_THEME_LIGHT = 1

        const val VIEW_TYPE_IMAGE = 0
        const val VIEW_TYPE_LOTTIE = 1
    }
    private var themeState = STATE_THEME_LIGHT
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconHolder {
        val inflater = LayoutInflater.from(parent.context)
        when(viewType) {
            VIEW_TYPE_IMAGE -> {
                val view = inflater.inflate(R.layout.toolbar_viewholder_icon, parent, false)
                return ImageIconHolder(view)
            }
            VIEW_TYPE_LOTTIE -> {
                val view = inflater.inflate(R.layout.toolbar_viewholder_icon_lottie, parent, false)
                return LottieIconHolder(view)
            }
        }
        val view = inflater.inflate(R.layout.toolbar_viewholder_icon, parent, false)
        return ImageIconHolder(view)
    }

    override fun onBindViewHolder(holder: IconHolder, position: Int) {
        val icon = iconConfig.iconList[position]
        holder.bind(icon, themeState)
    }

    override fun getItemCount(): Int {
        return iconConfig.iconList.size
    }

    override fun getItemViewType(position: Int): Int {
        val icon = iconConfig.iconList[position]
        when(icon.iconType) {
            IconToolbar.TYPE_IMAGE -> return VIEW_TYPE_IMAGE
            IconToolbar.TYPE_LOTTIE -> return TYPE_LOTTIE
        }
        return super.getItemViewType(position)
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

abstract class IconHolder(view: View): RecyclerView.ViewHolder(view) {
    abstract fun bind(iconToolbar: IconToolbar, themeState: Int)
}

class ImageIconHolder(view: View): IconHolder(view) {
    val iconImage = view.nav_icon_image
    val iconBadge = view.nav_icon_badge
    val context = itemView.context

    override fun bind(iconToolbar: IconToolbar, themeState: Int) {
        iconImage.tag = iconToolbar.id.toString()

        val unwrappedDrawable: Drawable? = ContextCompat.getDrawable(context, iconToolbar.imageRes)
        unwrappedDrawable?.let {
            val wrappedDrawable: Drawable = DrawableCompat.wrap(unwrappedDrawable)
            if (themeState == NavToolbarIconAdapter.STATE_THEME_DARK) {
                DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(context, R.color.white))
            } else if (themeState == NavToolbarIconAdapter.STATE_THEME_LIGHT) {
                DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(context, R.color.Neutral_N700))
            }
            iconImage.setImageDrawable(wrappedDrawable)
        }

        iconImage.setOnClickListener {
            iconToolbar.onIconClicked.invoke()
            if (iconToolbar.applink.isNotEmpty()) {
                RouteManager.route(context, iconToolbar.applink)
            }
        }

        if (iconToolbar.badgeCounter.isZero()) {
            iconBadge.visibility = View.GONE
        } else {
            iconBadge.visibility = View.VISIBLE
            iconBadge.text = iconToolbar.badgeCounter.toString()
        }
    }
}

class LottieIconHolder(view: View): IconHolder(view) {
    val iconImage = view.nav_icon_lottieav
    val iconBadge = view.nav_icon_badge_lottieav
    val context = itemView.context

    override fun bind(iconToolbar: IconToolbar, themeState: Int) {
        iconImage.tag = iconToolbar.id.toString()

        iconImage.cancelAnimation()
        iconImage.progress = 0f
        iconImage.setAnimation(iconToolbar.imageRes)
        iconImage.setOnClickListener {
            iconToolbar.onIconClicked.invoke()
            iconImage.playAnimation()
            if (iconToolbar.applink.isNotEmpty()) {
                RouteManager.route(context, iconToolbar.applink)
            }
        }

        if (iconToolbar.badgeCounter.isZero()) {
            iconBadge.visibility = View.GONE
        } else {
            iconBadge.visibility = View.VISIBLE
            iconBadge.text = iconToolbar.badgeCounter.toString()
        }
    }
}