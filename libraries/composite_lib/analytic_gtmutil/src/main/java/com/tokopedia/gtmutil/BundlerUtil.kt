package com.tokopedia.gtmutil

import android.os.Bundle
import java.util.*

object BundlerUtil    {
    fun putByte(key: String, value: Byte?, defaultValue: Byte, bundle: Bundle) {
        val byteValue = value ?: defaultValue
        bundle.putByte(key, byteValue)
    }

    fun putShort(key: String, value: Short?, defaultValue: Short, bundle: Bundle) {
        val shortValue = value ?: defaultValue
        bundle.putShort(key, shortValue)
    }

    fun putInt(key: String, value: Int?, defaultValue: Int, bundle: Bundle) {
        val intValue = value ?: defaultValue
        bundle.putInt(key, intValue)
    }

    fun putDouble(key: String, value: Double?, defaultValue: Double, bundle: Bundle) {
        val doubleValue = value ?: defaultValue
        bundle.putDouble(key, doubleValue)
    }

    fun putFloat(key: String, value: Float?, defaultValue: Float, bundle: Bundle) {
        val floatValue = value ?: defaultValue
        bundle.putFloat(key, floatValue)
    }

    fun putLong(key: String, value: Long?, defaultValue: Long, bundle: Bundle) {
        val longValue = value ?: defaultValue
        bundle.putLong(key, longValue)
    }

    fun putChar(key: String, value: Char?, defaultValue: Char, bundle: Bundle) {
        val chr = value ?: defaultValue
        bundle.putChar(key, chr)
    }

    fun putBoolean(key: String, value: Boolean?, defaultValue: Boolean, bundle: Bundle) {
        val bool = value ?: defaultValue
        bundle.putBoolean(key, bool)
    }

    fun putString(key: String, value: String?, defaultValue: String, bundle: Bundle) {
        val str = value ?: defaultValue
        bundle.putString(key, str)
    }

    fun putByteList(key: String, value: List<Byte>?, defaultValue: List<Byte>, bundle: Bundle) {
        val arr = value?.map { it.toInt() }?.toIntArray()
                ?: defaultValue.map { it.toInt() }.toIntArray()
        bundle.putIntArray(key, arr)
    }

    fun putShortList(key: String, value: List<Short>?, defaultValue: List<Short>, bundle: Bundle) {
        val arr = value?.map { it.toInt() }?.toIntArray()
                ?: defaultValue.map { it.toInt() }.toIntArray()
        bundle.putIntArray(key, arr)
    }

    fun putIntList(key: String, value: List<Int>?, defaultValue: List<Int>, bundle: Bundle) {
        val items = value ?: defaultValue
        bundle.putIntegerArrayList(key, ArrayList(items))
    }

    fun putDoubleList(key: String, value: List<Double>?, defaultValue: List<Double>, bundle: Bundle) {
        val arr = value?.toDoubleArray() ?: defaultValue.toDoubleArray()
        bundle.putDoubleArray(key, arr)
    }

    fun putFloatList(key: String, value: List<Float>?, defaultValue: List<Float>, bundle: Bundle) {
        val arr = value?.map { it.toDouble() }?.toDoubleArray()
                ?: defaultValue.map { it.toDouble() }.toDoubleArray()
        bundle.putDoubleArray(key, arr)
    }

    fun putLongList(key: String, value: List<Long>?, defaultValue: List<Long>, bundle: Bundle) {
        val arr = value?.toLongArray() ?: defaultValue.toLongArray()
        bundle.putLongArray(key, arr)
    }

    fun putCharList(key: String, value: List<Char>?, defaultValue: List<Char>, bundle: Bundle) {
        val arr: CharArray = value?.toCharArray() ?: defaultValue.toCharArray()
        bundle.putCharArray(key, arr)
    }

    fun putBooleanList(key: String, value: List<Boolean>?, defaultValue: List<Boolean>, bundle: Bundle) {
        val arr: BooleanArray = value?.toBooleanArray() ?: defaultValue.toBooleanArray()
        bundle.putBooleanArray(key, arr)
    }

    fun putStringList(key: String, value: List<String>?, defaultValue: List<String>, bundle: Bundle) {
        val items = value ?: defaultValue
        bundle.putStringArrayList(key, ArrayList(items))
    }
}
