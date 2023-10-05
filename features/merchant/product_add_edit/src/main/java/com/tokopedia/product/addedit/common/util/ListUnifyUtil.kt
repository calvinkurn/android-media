package com.tokopedia.product.addedit.common.util

import android.content.Context
import android.os.Build
import android.view.View
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.ListView
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.product.addedit.common.util.ListUnifyConstant.CATEGORY_ID_INDEX
import com.tokopedia.product.addedit.common.util.ListUnifyConstant.CONFIDENCE_INDEX
import com.tokopedia.product.addedit.common.util.ListUnifyConstant.DELIMITER
import com.tokopedia.product.addedit.common.util.ListUnifyConstant.PRECISION_INDEX
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify

object ListUnifyConstant{
    const val DELIMITER = ", "
    const val CATEGORY_ID_INDEX = 0
    const val CONFIDENCE_INDEX = 1
    const val PRECISION_INDEX = 2
}

internal fun ListItemUnify.getShownRadioButton() = run {
    if (listLeftRadiobtn?.visibility == View.VISIBLE) {
        listLeftRadiobtn
    } else if (listRightRadiobtn?.visibility == View.VISIBLE) {
        listRightRadiobtn
    } else {
        null
    }
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
    listActionText?.split(DELIMITER)?.getOrNull(CATEGORY_ID_INDEX).toLongOrZero()
}

internal fun ListItemUnify.getPrecision() = run {
    listActionText?.split(DELIMITER)?.getOrNull(PRECISION_INDEX).toDoubleOrZero()
}

internal fun ListItemUnify.getConfidence() = run {
    listActionText?.split(DELIMITER)?.getOrNull(CONFIDENCE_INDEX).toDoubleOrZero()
}

internal fun ListItemUnify.getCategoryName() = run {
    listTitleText.replace(" / ", "/")
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
    val unifyBlackColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950)
    it.listTitle?.setTextColor(unifyBlackColor)
}

internal fun ListItemUnify.setFixedTextColorToUnify(context: Context) = let {
    val unifyGrayColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN400)
    it.listTitle?.setTextColor(unifyGrayColor)
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

internal fun ListUnify.setToFixedText(text: String, context: Context) = apply {
    val selectedList = ArrayList<ListItemUnify>()
    val selectedItemUnify = ListItemUnify(text, "")
    selectedItemUnify.isBold = false
    selectedList.add(selectedItemUnify)
    setData(selectedList)
    show()
    onLoadFinish { selectedItemUnify.setFixedTextColorToUnify(context) }
}
