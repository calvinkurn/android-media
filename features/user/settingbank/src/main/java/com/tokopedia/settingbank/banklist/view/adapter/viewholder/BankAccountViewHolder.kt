package com.tokopedia.settingbank.banklist.view.adapter.viewholder

import android.content.Context
import androidx.appcompat.widget.PopupMenu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.banklist.view.listener.BankAccountPopupListener
import com.tokopedia.settingbank.banklist.view.viewmodel.BankAccountViewModel

/**
 * @author by nisie on 6/12/18.
 */
class BankAccountViewHolder(val v: View, val listener: BankAccountPopupListener) :
        AbstractViewHolder<BankAccountViewModel>(v) {

    private val MENU_MAKE_DEFAULT: Int = 101
    private val MENU_EDIT: Int = 102
    private val MENU_DELETE: Int = 103


    val bankIcon: ImageView = itemView.findViewById(R.id.bank_icon)
    val bankName: TextView = itemView.findViewById(R.id.bank_name)
    val accountNumber: TextView = itemView.findViewById(R.id.account_number)
    val accountName: TextView = itemView.findViewById(R.id.account_name)
    val dropDownMenu: ImageView = itemView.findViewById(R.id.dropdown_menu)
    val mainAccount: TextView = itemView.findViewById(R.id.main_account)

    lateinit var popupMenu: PopupMenu

    companion object {
        val LAYOUT = R.layout.item_bank_account
    }

    override fun bind(element: BankAccountViewModel?) {
        if (element != null) {

            bankName.text = MethodChecker.fromHtml(element.bankName)
            accountName.text = MethodChecker.fromHtml(element.accountName)
            accountNumber.text = element.accountNumber

            if (element.isDefaultBank != null && element.isDefaultBank!!) {
                mainAccount.visibility = View.VISIBLE
            } else {
                mainAccount.visibility = View.GONE
            }

            if (!element.bankLogo.isNullOrBlank()) {
                bankIcon.visibility = View.VISIBLE
                ImageHandler.LoadImage(bankIcon, element.bankLogo)
            } else {
                bankIcon.visibility = View.INVISIBLE
            }

            dropDownMenu.setOnClickListener { v: View? ->
                if (v != null) {
                    dropDownMenuClickListener(dropDownMenu
                            .context, v, element)
                }
            }
        }
    }

    private fun dropDownMenuClickListener(context: Context, v: View, element: BankAccountViewModel?) {

        if (!::popupMenu.isInitialized) {
            popupMenu = PopupMenu(context, v)
            popupMenu.setOnMenuItemClickListener { item: MenuItem? ->
                onPopupMenuClicked(item)
            }
        }
        setupPopupMenu(element, popupMenu, context)
        popupMenu.show()
    }


    private fun onPopupMenuClicked(item: MenuItem?): Boolean {
        return if (item != null) {
            when (item.itemId) {
                MENU_MAKE_DEFAULT -> {
                    listener.makeMainAccount(adapterPosition)
                    return true
                }
                MENU_EDIT -> {
                    listener.editBankAccount(adapterPosition)
                    return true
                }
                MENU_DELETE -> {
                    listener.deleteBankAccount(adapterPosition)
                    return true
                }
                else -> false
            }
        } else {

            false
        }
    }

    private fun setupPopupMenu(element: BankAccountViewModel?, popupMenu: PopupMenu, context: Context) {
        if (element != null) {

            popupMenu.menu.clear()

            if (element.isDefaultBank != null && !element.isDefaultBank!!) {
                popupMenu.menu.add(1, MENU_MAKE_DEFAULT, 1,
                        context.getString(R.string.menu_make_main_account))
            }
            popupMenu.menu.add(1, MENU_EDIT, 2, context.getString(R.string.menu_edit))
            popupMenu.menu.add(1, MENU_DELETE, 3, context.getString(R.string.menu_delete))

        }
    }
}