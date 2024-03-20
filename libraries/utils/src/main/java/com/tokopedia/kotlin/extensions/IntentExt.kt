package com.tokopedia.kotlin.extensions

import android.app.Activity
import android.content.Intent

/**
 * Created by yovi.putra on 3/20/24.
 * Copyright (c) 2024 android-tokopedia-core All rights reserved.
 */
fun Intent.extraGetString(
    key: String,
    defaultValue: String = ""
) = extras?.getString(key, defaultValue) ?: defaultValue

fun Intent.extraGetBoolean(
    key: String,
    defaultValue: Boolean = false
) = extras?.getBoolean(key, defaultValue) ?: defaultValue

/**
 * get intent extras data
 */
fun Intent.getString(
    key: String,
    defaultValue: String = ""
) = getStringExtra(key) ?: defaultValue

fun Intent.getBoolean(
    key: String,
    defaultValue: Boolean = false
) = getBooleanExtra(key, defaultValue)

/**
 * get activity intent / intent extras data
 */
fun Activity.extraGetString(
    key: String,
    defaultValue: String = ""
) = intent.extraGetString(key, defaultValue)

fun Activity.extraGetBoolean(
    key: String,
    defaultValue: Boolean = false
) = intent.extraGetBoolean(key, defaultValue)

fun Activity.intentGetString(
    key: String,
    defaultValue: String = ""
) = intent.getString(key, defaultValue)

fun Activity.intentGetBoolean(
    key: String,
    defaultValue: Boolean = false
) = intent.getBoolean(key, defaultValue)
