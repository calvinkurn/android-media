package com.tokopedia.product.addedit.common.util

import android.content.Context
import android.os.Build
import android.view.View
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.ListView
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.addedit.R
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify

internal fun ListItemUnify.getShownRadioButton()= run {
    if (listLeftRadiobtn?.visibility == View.VISIBLE) listLeftRadiobtn
    else if (listRightRadiobtn?.visibility == View.VISIBLE) listRightRadiobtn
    else null
}

internal fun ListUnify.getSelected(items: List<ListItemUnify>) = run {
    when (choiceMode) {
        // for radio button type
        ListView.CHOICE_MODE_SINGLE -> {
            items.firstOrNull { it.listRightRadiobtn?.isChecked ?: false || it.listLeftRadiobtn?.isChecked ?: false }
        }
        else -> {
            null
        }
    }
}

internal fun ListUnify.setSelected(items: List<ListItemUnify>, position: Int, onChecked: (selectedItem: ListItemUnify) -> Any) = run {
    val selectedItem = this.getItemAtPosition(position) as ListItemUnify

    when (choiceMode) {
        // for radio button type
        ListView.CHOICE_MODE_SINGLE -> {
            // deselect previously selected item
            items.filter { it.getShownRadioButton()?.isChecked ?: false }
                    .filterNot { it == selectedItem }
                    .onEach { it.getShownRadioButton()?.isChecked = false }
            selectedItem.getShownRadioButton()?.isChecked = true
        }
    }

    onChecked(selectedItem)
}

internal fun ListItemUnify.getCategoryId() = run {
    listActionText?.toLong().orZero()
}

internal fun ListItemUnify.setPrimarySelected(context: Context?, isChecked: Boolean) = let {
    context?.apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            it.listRightRadiobtn?.isChecked = isChecked
        }
        if (isChecked) {
            val label = Label(this)
            val parentLayout = (it.listTitle?.parent as LinearLayout)
            label.setLabel(getString(com.tokopedia.product.addedit.R.string.label_primary))
            label.setLabelType(Label.GENERAL_DARK_GREEN)
            parentLayout.addView(label, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
            parentLayout.orientation = LinearLayout.HORIZONTAL
        }
    }
}

// set color to dark mode support
internal fun ListItemUnify.setTextColorToUnify(context: Context) = let {
    val unifyBlackColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700)
    it.listTitle?.setTextColor(unifyBlackColor)

}

internal fun ListItemUnify.getCategoryName() = run {
    listTitleText.replace(" / ", "/")
}

internal fun ListUnify.setToDisplayText(text: String, context: Context) = apply {
    val selectedList = ArrayList<ListItemUnify>()
    val selectedItemUnify = ListItemUnify(text, "")
    selectedItemUnify.isBold = false
    selectedList.add(selectedItemUnify)
    setData(selectedList)
    show()
    onLoadFinish { selectedItemUnify.setTextColorToUnify(context) }
}