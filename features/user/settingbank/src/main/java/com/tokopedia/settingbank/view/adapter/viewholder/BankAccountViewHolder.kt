package com.tokopedia.settingbank.view.adapter.viewholder

import android.content.Context
import android.support.v7.widget.PopupMenu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.core.app.MainApplication
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.view.listener.BankAccountPopupListener
import com.tokopedia.settingbank.view.viewmodel.BankAccountViewModel

/**
 * @author by nisie on 6/12/18.
 */
class BankAccountViewHolder(val v: View, val listener: BankAccountPopupListener) :
        AbstractViewHolder<BankAccountViewModel>(v) {


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

            if (element.isDefaultBank) {
                mainAccount.visibility = View.VISIBLE
            } else {
                mainAccount.visibility = View.GONE
            }

            ImageHandler.LoadImage(bankIcon, element.bankLogo)

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
                onPopupMenuClicked(item,
                        element)
            }
        }
        setupPopupMenu(element, popupMenu)
        popupMenu.show()
    }

    private fun onPopupMenuClicked(item: MenuItem?, element: BankAccountViewModel?): Boolean {
        return if (item != null) {
            when (item.itemId) {
                R.id.menu_make_main_account -> {
                    listener.makeMainAccount(adapterPosition, element)
                    return true
                }
                R.id.menu_edit -> {
                    listener.editBankAccount(adapterPosition, element)
                    return true
                }
                R.id.menu_delete -> {
                    listener.deleteBankAccount(adapterPosition, element)
                    return true
                }
                else -> false
            }
        } else {

            false
        }
    }

    private fun setupPopupMenu(element: BankAccountViewModel?, popupMenu: PopupMenu) {
        if (element != null) {

            popupMenu.menu.clear()

            if (!element.isDefaultBank) {
                popupMenu.menu.add(1, R.id.menu_make_main_account, 1, MainApplication.getAppContext()
                        .getString(R.string.menu_make_main_account))
            }
            popupMenu.menu.add(1, R.id.menu_edit, 2, MainApplication.getAppContext()
                    .getString(R.string.menu_edit))
            popupMenu.menu.add(1, R.id.menu_delete, 3, MainApplication.getAppContext()
                    .getString(R.string.menu_delete))

        }
    }
}