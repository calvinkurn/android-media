package com.tokopedia.sellerpersona.view.compose.common

import android.util.AttributeSet

/**
 * Created by @ilhamsuaib on 13/07/23.
 */

internal fun getAttributeSet(): AttributeSet {
    return object : AttributeSet {
        override fun getAttributeCount(): Int {
            return 0
        }

        override fun getAttributeName(index: Int): String? {
            return null
        }

        override fun getAttributeValue(index: Int): String? {
            return null
        }

        override fun getAttributeValue(namespace: String, name: String): String? {
            return null
        }

        override fun getPositionDescription(): String? {
            return null
        }

        override fun getAttributeNameResource(index: Int): Int {
            return 0
        }

        override fun getAttributeListValue(
            namespace: String,
            attribute: String,
            options: Array<String>,
            defaultValue: Int
        ): Int {
            return 0
        }

        override fun getAttributeBooleanValue(
            namespace: String,
            attribute: String,
            defaultValue: Boolean
        ): Boolean {
            return false
        }

        override fun getAttributeResourceValue(
            namespace: String,
            attribute: String,
            defaultValue: Int
        ): Int {
            return 0
        }

        override fun getAttributeIntValue(
            namespace: String,
            attribute: String,
            defaultValue: Int
        ): Int {
            return 0
        }

        override fun getAttributeUnsignedIntValue(
            namespace: String,
            attribute: String,
            defaultValue: Int
        ): Int {
            return 0
        }

        override fun getAttributeFloatValue(
            namespace: String,
            attribute: String,
            defaultValue: Float
        ): Float {
            return 0f
        }

        override fun getAttributeListValue(
            index: Int,
            options: Array<String>,
            defaultValue: Int
        ): Int {
            return 0
        }

        override fun getAttributeBooleanValue(index: Int, defaultValue: Boolean): Boolean {
            return false
        }

        override fun getAttributeResourceValue(index: Int, defaultValue: Int): Int {
            return 0
        }

        override fun getAttributeIntValue(index: Int, defaultValue: Int): Int {
            return 0
        }

        override fun getAttributeUnsignedIntValue(index: Int, defaultValue: Int): Int {
            return 0
        }

        override fun getAttributeFloatValue(index: Int, defaultValue: Float): Float {
            return 0f
        }

        override fun getIdAttribute(): String? {
            return null
        }

        override fun getClassAttribute(): String? {
            return null
        }

        override fun getIdAttributeResourceValue(defaultValue: Int): Int {
            return 0
        }

        override fun getStyleAttribute(): Int {
            return 0
        }
    }
}