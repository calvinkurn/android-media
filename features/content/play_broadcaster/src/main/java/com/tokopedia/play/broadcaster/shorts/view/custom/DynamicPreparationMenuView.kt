package com.tokopedia.play.broadcaster.shorts.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ViewDynamicPreparationMenuBinding

/**
 * Created By : Jonathan Darwin on November 09, 2022
 */
class DynamicPreparationMenuView : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private val binding = ViewDynamicPreparationMenuBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private var onClick: ((DynamicPreparationMenu) -> Unit)? = null

    private val adapter = DynamicPreparationMenuAdapter {
        onClick?.invoke(it)
    }

    init {
        binding.rvMenu.layoutManager = LinearLayoutManager(context)
        binding.rvMenu.addItemDecoration(DynamicPreparationMenuItemDecoration(context))
        binding.rvMenu.adapter = adapter
    }

    fun submitMenu(menuList: List<DynamicPreparationMenu>) {
        val finalMenuList = menuList.map {
            DynamicPreparationMenuAdapter.Item(
                data = it,
                isShow = true
            )
        }
        adapter.setItemsAndAnimateChanges(finalMenuList)
    }

    fun showMenuText(isShow: Boolean) {
        val finalMenuList = adapter.getItems().map {
            it.copy(
                isShow = isShow
            )
        }
        adapter.setItemsAndAnimateChanges(finalMenuList)
    }
    fun getMenuView(
        menu: DynamicPreparationMenu.Menu,
        menuView: (menuView: View?) -> Unit,
    ) {
        binding.rvMenu.addOneTimeGlobalLayoutListener {
            val menuIdx = adapter.getItems().indexOfFirst {
                it.data.menu.id == menu.id
            }
            if (menuIdx == -1) {
                menuView.invoke(null)
                return@addOneTimeGlobalLayoutListener
            }

            val holder = binding.rvMenu.findViewHolderForAdapterPosition(menuIdx)
            val menuItem = holder?.itemView?.findViewById<IconUnify>(R.id.ic_menu)
            menuView.invoke(menuItem)
        }
    }

    fun setOnMenuClickListener(onClick: (DynamicPreparationMenu) -> Unit) {
        this.onClick = onClick
    }
}
