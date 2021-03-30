package com.tokopedia.searchbar.navigation_component

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.tokopedia.applink.RouteManager
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.searchbar.R
import com.tokopedia.searchbar.navigation_component.analytics.NavToolbarTracking
import com.tokopedia.searchbar.navigation_component.icons.IconConfig
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.searchbar.navigation_component.icons.IconToolbar
import com.tokopedia.searchbar.navigation_component.listener.TopNavComponentListener
import com.tokopedia.unifycomponents.NotificationUnify
import kotlinx.android.synthetic.main.toolbar_viewholder_icon.view.*
import kotlinx.android.synthetic.main.toolbar_viewholder_icon_animated.view.*
import kotlinx.android.synthetic.main.toolbar_viewholder_icon_lottie.view.*

internal class NavToolbarIconAdapter(private var iconConfig: IconConfig,
                                     private val topNavComponentListener: TopNavComponentListener)
    : RecyclerView.Adapter<IconHolder>() {
    companion object {
        const val STATE_THEME_DARK = 0
        const val STATE_THEME_LIGHT = 1

        const val VIEW_TYPE_IMAGE = 0
        const val VIEW_TYPE_LOTTIE = 1
        const val VIEW_TYPE_ANIMATED = 2
    }

    private var iconRecyclerView: RecyclerView? = null
    private var themeState = STATE_THEME_LIGHT
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconHolder {
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            VIEW_TYPE_IMAGE -> {
                val view = inflater.inflate(R.layout.toolbar_viewholder_icon, parent, false)
                return ImageIconHolder(view, topNavComponentListener)
            }
            VIEW_TYPE_LOTTIE -> {
                val view = inflater.inflate(R.layout.toolbar_viewholder_icon_lottie, parent, false)
                return LottieIconHolder(view, topNavComponentListener)
            }
            VIEW_TYPE_ANIMATED -> {
                val view = inflater.inflate(R.layout.toolbar_viewholder_icon_animated, parent, false)
                return AnimatedIconHolder(view, topNavComponentListener)
            }
        }
        val view = inflater.inflate(R.layout.toolbar_viewholder_icon, parent, false)
        return ImageIconHolder(view, topNavComponentListener)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.iconRecyclerView = recyclerView
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
        when (icon.iconType) {
            IconToolbar.TYPE_IMAGE -> return VIEW_TYPE_IMAGE
            IconToolbar.TYPE_LOTTIE -> return VIEW_TYPE_LOTTIE
            IconToolbar.TYPE_ANIMATED -> return VIEW_TYPE_ANIMATED
        }
        return super.getItemViewType(position)
    }

    override fun getItemId(position: Int): Long {
        return iconConfig.iconList[position].id.toLong()
    }

    fun resetIcon(iconConfig: IconConfig) {
        this.iconConfig = iconConfig
        notifyDataSetChanged()
    }

    fun setIconCounter(iconId: Int, counter: Int) {
        val selectedIcon = this.iconConfig.iconList.find { it.id == iconId }
        val selectedIconPosition = this.iconConfig.iconList.indexOf(selectedIcon)
        selectedIcon?.badgeCounter = counter
        selectedIcon?.let {
            this.iconConfig.iconList[selectedIconPosition] = selectedIcon
            notifyItemChanged(selectedIconPosition)
        }
    }

    fun triggerLottieAnimation(lottieIconId: Int) {
        val selectedIcon = this.iconConfig.iconList.find { it.id == lottieIconId }
        val selectedIconPosition = this.iconConfig.iconList.indexOf(selectedIcon)
        val iconViewHolder = iconRecyclerView?.findViewHolderForAdapterPosition(selectedIconPosition)
        iconViewHolder?.let {
            (it as? LottieIconHolder)?.iconImage?.playAnimation()
        }
    }

    fun triggerAnimatedVectorDrawableAnimation(animatedIconId: Int) {
        val selectedIcon = this.iconConfig.iconList.find { it.id == animatedIconId }
        val selectedIconPosition = this.iconConfig.iconList.indexOf(selectedIcon)
        val iconViewHolder = iconRecyclerView?.findViewHolderForAdapterPosition(selectedIconPosition)
        iconViewHolder?.let { viewHolder ->
            selectedIcon?.let { iconToolbar ->
                (viewHolder as? AnimatedIconHolder)?.playAnimation(iconToolbar)
            }
        }
    }

    fun setThemeState(newState: Int) {
        this.themeState = newState
        notifyDataSetChanged()
    }

    fun getGlobalNavIconPosition(): Int? {
        val model = iconConfig.iconList.find { it.id == IconList.ID_NAV_GLOBAL }
        model?.let { return iconConfig.iconList.indexOf(model) }
        return null
    }
    fun getInboxIconPosition(): Int? {
        val model = iconConfig.iconList.find { it.id == IconList.ID_INBOX }
        model?.let { return iconConfig.iconList.indexOf(model) }
        return null
    }
}

internal abstract class IconHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(iconToolbar: IconToolbar, themeState: Int)
}

internal class ImageIconHolder(view: View, val topNavComponentListener: TopNavComponentListener) : IconHolder(view) {
    val iconImage = view.nav_icon_image
    val context = itemView.context

    override fun bind(iconToolbar: IconToolbar, themeState: Int) {
        iconImage.tag = iconToolbar.id.toString()

        if (iconToolbar.imageRes != null) {
            val unwrappedDrawable: Drawable? = ContextCompat.getDrawable(context, iconToolbar.imageRes)
            unwrappedDrawable?.let {
                val wrappedDrawable: Drawable = DrawableCompat.wrap(unwrappedDrawable)
                if (themeState == NavToolbarIconAdapter.STATE_THEME_DARK) {
                    if (iconToolbar.imageRes != IconList.ID_INBOX) {
                        DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
                    } else {
                        val unifyColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0)
                        unwrappedDrawable.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(unifyColor, BlendModeCompat.SRC_ATOP)
                    }
                } else if (themeState == NavToolbarIconAdapter.STATE_THEME_LIGHT) {
                    if (iconToolbar.imageRes != IconList.ID_INBOX) {
                        DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700))
                    } else {
                        val unifyColor = ContextCompat.getColor(context, R.color.searchbar_dms_state_light_icon)
                        unwrappedDrawable.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(unifyColor, BlendModeCompat.SRC_ATOP)
                    }
                }
                iconImage.imageDrawable = wrappedDrawable
            }
        } else {
            if (themeState == NavToolbarIconAdapter.STATE_THEME_DARK) {
                val drawable = getIconUnifyDrawable(
                        context = context,
                        iconId = iconToolbar.id,
                        assetColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0)
                )
                iconImage.imageDrawable = drawable
            } else if (themeState == NavToolbarIconAdapter.STATE_THEME_LIGHT) {
                val drawable = getIconUnifyDrawable(
                        context = context,
                        iconId = iconToolbar.id,
                        assetColor = ContextCompat.getColor(context, R.color.searchbar_dms_state_light_icon)
                )
                iconImage.imageDrawable = drawable
            }
        }

        iconImage.setOnClickListener {
            if (!iconToolbar.disableDefaultGtmTracker) {
                NavToolbarTracking.clickNavToolbarComponent(
                        pageName = topNavComponentListener.getPageName(),
                        componentName = iconToolbar.name,
                        userId = topNavComponentListener.getUserId()
                )
            }
            iconToolbar.onIconClicked.invoke()
            val isLoggedIn = topNavComponentListener.isLoggedIn()
            if (!iconToolbar.disableRouteManager && iconToolbar.applink.isNotEmpty() && isLoggedIn) {
                RouteManager.route(context, iconToolbar.bundle, iconToolbar.applink, null)
            } else if (!iconToolbar.disableRouteManager && iconToolbar.nonLoginApplink.isNotEmpty() && !isLoggedIn) {
                RouteManager.route(context, iconToolbar.bundle, iconToolbar.nonLoginApplink, null)
            }
        }

        if (iconToolbar.badgeCounter.isZero()) {
            iconImage.notificationRef.gone()
        } else {
            iconImage.notificationRef.setNotification(
                    notif = iconToolbar.badgeCounter.toString(),
                    notificationType = NotificationUnify.COUNTER_TYPE,
                    colorType = NotificationUnify.COLOR_PRIMARY
            )
            iconImage.notificationGravity = Gravity.TOP or Gravity.RIGHT
            iconImage.notificationRef.visible()
        }
        iconImage.setNotifXY(1f, -0.8f)
        iconImage.visibility = View.VISIBLE
    }

    private fun constructCounterTagById(id: Int) =
            context.getString(R.string.tag_counter_id) + id
}

internal class LottieIconHolder(view: View, val topNavComponentListener: TopNavComponentListener) : IconHolder(view) {
    val iconImage = view.nav_icon_lottieav
    val iconBadge = view.nav_icon_badge_lottieav
    val context = itemView.context

    override fun bind(iconToolbar: IconToolbar, themeState: Int) {
        iconImage.tag = iconToolbar.id.toString()
        iconBadge.tag = constructCounterTagById(iconToolbar.id)
        iconImage.cancelAnimation()
        iconImage.progress = 0f
        iconToolbar.imageRes?.let { iconImage.setAnimation(iconToolbar.imageRes) }
        iconImage.setOnClickListener {
            if (!iconToolbar.disableDefaultGtmTracker) {
                NavToolbarTracking.clickNavToolbarComponent(
                        pageName = topNavComponentListener.getPageName(),
                        componentName = iconToolbar.name,
                        userId = topNavComponentListener.getUserId()
                )
            }
            iconToolbar.onIconClicked.invoke()
            val isLoggedIn = topNavComponentListener.isLoggedIn()
            if (!iconToolbar.disableRouteManager && iconToolbar.applink.isNotEmpty() && isLoggedIn) {
                RouteManager.route(context, iconToolbar.bundle, iconToolbar.applink, null)
            } else if (!iconToolbar.disableRouteManager && iconToolbar.nonLoginApplink.isNotEmpty() && !isLoggedIn) {
                RouteManager.route(context, iconToolbar.bundle, iconToolbar.nonLoginApplink, null)
            }
        }

        if (iconToolbar.badgeCounter.isZero()) {
            iconBadge.visibility = View.GONE
        } else {
            iconBadge.visibility = View.VISIBLE
            iconBadge.text = iconToolbar.badgeCounter.toString()
        }

        if (iconToolbar.paddingEndRes != 0) {
            iconImage.setPadding(0, 0, itemView.resources.getDimensionPixelOffset(iconToolbar.paddingEndRes), 0)
        }
    }

    private fun constructCounterTagById(id: Int) =
            context.getString(R.string.tag_counter_id) + id
}

internal class AnimatedIconHolder(view: View, val topNavComponentListener: TopNavComponentListener) : IconHolder(view) {
    val iconImage = view.nav_icon_idle
    val iconAnimatedImage = view.nav_icon_view
    val context = itemView.context

    override fun bind(iconToolbar: IconToolbar, themeState: Int) {
        iconAnimatedImage.tag = iconToolbar.id.toString()

        if (themeState == NavToolbarIconAdapter.STATE_THEME_DARK) {
            val drawable = getIconUnifyDrawable(
                    context = context,
                    iconId = iconToolbar.id,
                    assetColor = ContextCompat.getColor(context, R.color.Unify_N0)
            )
            iconImage.imageDrawable = drawable
        } else if (themeState == NavToolbarIconAdapter.STATE_THEME_LIGHT) {
            val drawable = getIconUnifyDrawable(
                    context = context,
                    iconId = iconToolbar.id,
                    assetColor = ContextCompat.getColor(context, R.color.searchbar_dms_state_light_icon)
            )
            iconImage.imageDrawable = drawable
        }

        iconToolbar.imageRes?.let {
            iconAnimatedImage.setBackgroundResource(it)
        }

        iconImage.setOnClickListener {
            onClickAnimatedImage(iconToolbar)
        }

        iconAnimatedImage.setOnClickListener {
            onClickAnimatedImage(iconToolbar)
        }

        if (iconToolbar.badgeCounter.isZero()) {
            iconImage.notificationRef.gone()
        } else {
            iconImage.notificationRef.setNotification(
                    notif = iconToolbar.badgeCounter.toString(),
                    notificationType = NotificationUnify.COUNTER_TYPE,
                    colorType = NotificationUnify.COLOR_PRIMARY
            )
            iconImage.notificationGravity = Gravity.TOP or Gravity.RIGHT
            iconImage.notificationRef.visible()
        }
        iconImage.setNotifXY(1f, -0.8f)

        if (iconToolbar.paddingEndRes != 0) {
            iconAnimatedImage.setPadding(0, 0, itemView.resources.getDimensionPixelOffset(iconToolbar.paddingEndRes), 0)
        }

        iconAnimatedImage.gone()
        iconImage.show()
    }

    private fun onClickAnimatedImage(iconToolbar: IconToolbar) {
        if (!iconToolbar.disableDefaultGtmTracker) {
            NavToolbarTracking.clickNavToolbarComponent(
                    pageName = topNavComponentListener.getPageName(),
                    componentName = iconToolbar.name,
                    userId = topNavComponentListener.getUserId()
            )
        }
        iconToolbar.onIconClicked.invoke()
        val isLoggedIn = topNavComponentListener.isLoggedIn()
        if (!iconToolbar.disableRouteManager && iconToolbar.applink.isNotEmpty() && isLoggedIn) {
            RouteManager.route(context, iconToolbar.bundle, iconToolbar.applink, null)
        } else if (!iconToolbar.disableRouteManager && iconToolbar.nonLoginApplink.isNotEmpty() && !isLoggedIn) {
            RouteManager.route(context, iconToolbar.bundle, iconToolbar.nonLoginApplink, null)
        }
    }

    @SuppressLint("AnimatedVectorDrawable")
    fun playAnimation(iconToolbar: IconToolbar) {
        iconToolbar.imageRes?.let {
            iconImage.gone()
            iconAnimatedImage.show()

            val animation: AnimatedVectorDrawableCompat? = AnimatedVectorDrawableCompat.create(context, it)
            if (animation != null) {
                iconAnimatedImage.background = animation
                animation.start()

                Handler().postDelayed({
                    iconAnimatedImage.gone()
                    iconImage.show()
                }, 1000)
            } else {
                iconAnimatedImage.gone()
                iconImage.show()
            }
        }
    }

    private fun constructCounterTagById(id: Int) = context.getString(R.string.tag_counter_id) + id
}