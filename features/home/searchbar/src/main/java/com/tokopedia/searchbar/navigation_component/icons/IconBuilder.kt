package com.tokopedia.searchbar.navigation_component.icons

class IconBuilder {
    private val listIcon = mutableListOf<IconToolbar>()

    fun build(): IconConfig {
        return IconConfig(listIcon)
    }

    //image icon
    fun addMessageIcon(onClick: ()->Unit): IconBuilder { listIcon.add(IconList.MessageIcon.get(onClick)); return this}
    fun addNotificationIcon(onClick: ()->Unit): IconBuilder { listIcon.add(IconList.NotificationIcon.get(onClick)); return this }
    fun addCartIcon(onClick: ()->Unit): IconBuilder { listIcon.add(IconList.CartIcon.get(onClick)); return this }
    fun addNavGlobalIcon(onClick: ()->Unit): IconBuilder { listIcon.add(IconList.NavGlobalIcon.get(onClick)); return this }

    //lottie icon
    fun addLottieWishlistIcon(onClick: ()->Unit): IconBuilder { listIcon.add(IconList.LottieWishlistIcon.get(onClick)); return this }
}