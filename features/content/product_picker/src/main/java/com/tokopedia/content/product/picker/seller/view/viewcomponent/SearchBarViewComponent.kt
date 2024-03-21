package com.tokopedia.content.product.picker.seller.view.viewcomponent

import com.tokopedia.content.common.util.eventbus.EventBus
import com.tokopedia.content.product.picker.seller.view.custom.ContentProductSearchBar
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by kenny.hadisaputra on 28/01/22
 */
internal class SearchBarViewComponent(
    private val view: ContentProductSearchBar,
    eventBus: EventBus<in Event>,
) : ViewComponent(view) {

    init {
        view.setListener(object : ContentProductSearchBar.Listener {
            override fun onSearchBarClicked(view: ContentProductSearchBar) {
                eventBus.emit(Event.OnSearchBarClicked)
            }

            override fun onNewKeyword(view: ContentProductSearchBar, keyword: String) {
                eventBus.emit(Event.OnSearched(keyword))
            }

            override fun onCleared(view: ContentProductSearchBar) {
                eventBus.emit(Event.OnSearched(""))
            }
        })
    }

    fun setKeyword(keyword: String) {
        view.text = keyword
    }

    fun setPlaceholder(placeholder: String) {
        view.setHint(placeholder)
    }

    override fun hide() {
        super.hide()
        view.clear()
    }

    sealed class Event {
        data class OnSearched(val keyword: String) : Event()
        object OnSearchBarClicked : Event()
    }
}
