package com.tokopedia.sellerhome.settings.view.uimodel

import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.list.ListItemUnify

class MenuItem(val title: String,
                   val description: String? = null,
                   val unifyVariant: Int,
                   val drawableReference: Int)

fun mapMenuItem(menuItem: MenuItem) : ListItemUnify =
        with(menuItem) {
            ListItemUnify(title, description.toBlankOrString())
                    .apply {
                        setVariant(unifyVariant)
                        listDrawableReference = drawableReference
                    }
        }

fun mapMenuItemList(menuItemList: List<MenuItem>) : List<ListItemUnify> {
    val listItemUnifyList: MutableList<ListItemUnify> = mutableListOf()
    menuItemList.forEach { menuItem ->
        listItemUnifyList.add(mapMenuItem(menuItem))
    }
    return listItemUnifyList
}