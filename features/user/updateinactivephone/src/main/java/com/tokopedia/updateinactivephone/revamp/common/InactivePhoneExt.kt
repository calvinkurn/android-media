package com.tokopedia.updateinactivephone.revamp.common

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

inline fun FragmentManager.doTransaction(func: FragmentTransaction.() ->
FragmentTransaction) {
    beginTransaction().func().commit()
}

fun AppCompatActivity.addFragment(frameId: Int, fragment: Fragment) {
    supportFragmentManager.doTransaction {
        addToBackStack(null)
        add(frameId, fragment)
    }
}

fun AppCompatActivity.replaceFragment(frameId: Int, fragment: Fragment) {
    supportFragmentManager.doTransaction {
        addToBackStack(null)
        replace(frameId, fragment)
    }
}

fun AppCompatActivity.removeFragment(fragment: Fragment) {
    supportFragmentManager.doTransaction { remove(fragment) }
}