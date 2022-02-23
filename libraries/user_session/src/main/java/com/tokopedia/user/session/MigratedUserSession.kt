package com.tokopedia.user.session

import android.content.Context
import android.util.Pair
import com.tokopedia.user.session.datastore.UserSessionDataStore
import com.tokopedia.user.session.datastore.UserSessionDataStoreClient
import com.tokopedia.user.session.util.EncoderDecoder

open class MigratedUserSession(var context: Context?) {

    private var userSessionDataStore: UserSessionDataStore? = null
    	get() = UserSessionDataStoreClient.getInstance(context!!)

    protected fun getLong(prefName: String?, keyName: String?, defValue: Long): Long {
	val newPrefName = String.format("%s%s", prefName, suffix)
	val newKeyName = String.format("%s%s", keyName, suffix)

	// look up from cache
	val key = Pair(newPrefName, newKeyName)
	if (UserSessionMap.map.containsKey(key)) {
	    try {
		val value = UserSessionMap.map[key]
		return if (value == null) {
		    defValue
		} else {
		    value as Long
		}
	    } catch (ignored: Exception) {
	    }
	}
	val oldValue = internalGetLong(prefName, keyName, defValue)
	if (oldValue != defValue) {
	    internalCleanKey(prefName, keyName)
	    internalSetLong(newPrefName, newKeyName, oldValue)
	    UserSessionMap.map[key] = oldValue
	    return oldValue
	}
	val sharedPrefs = context?.getSharedPreferences(newPrefName, Context.MODE_PRIVATE)
	val value = sharedPrefs?.getLong(newKeyName, defValue) ?: defValue
	UserSessionMap.map[key] = value
	return value
    }

    private fun internalGetLong(prefName: String?, keyName: String?, defValue: Long): Long {
	val sharedPrefs = context?.getSharedPreferences(prefName, Context.MODE_PRIVATE)
	return sharedPrefs?.getLong(keyName, defValue) ?: defValue
    }

    private fun internalSetLong(prefName: String, keyName: String, value: Long) {
	val sharedPrefs = context?.getSharedPreferences(prefName, Context.MODE_PRIVATE)
	val editor = sharedPrefs?.edit()
	editor?.putLong(keyName, value)
	editor?.apply()
    }

    fun convertToNewKey(prefName: String?, keyName: String?): Pair<String, String> {
	return Pair(String.format("%s%s", prefName, suffix), String.format("%s%s", keyName, suffix))
    }

    protected fun setLong(prefName: String, keyName: String, value: Long) {
	var prefName = prefName
	var keyName = keyName
	val newKeys = convertToNewKey(prefName, keyName)
	prefName = newKeys.first
	keyName = newKeys.second
	UserSessionMap.map[Pair(prefName, keyName)] = value
	internalSetLong(prefName, keyName, value)
    }

    protected fun cleanKey(prefName: String?, keyName: String?) {
	var prefName = prefName
	var keyName = keyName
	val newKeys = convertToNewKey(prefName, keyName)
	prefName = newKeys.first
	keyName = newKeys.second
	UserSessionMap.map.remove(Pair(prefName, keyName))
	internalCleanKey(prefName, keyName)
    }

    fun internalCleanKey(prefName: String?, keyName: String?) {
	val sharedPrefs = context?.getSharedPreferences(prefName, Context.MODE_PRIVATE)
	val editor = sharedPrefs?.edit()
	editor?.remove(keyName)?.apply()
    }

    protected fun nullString(prefName: String?, keyName: String?) {
//	var prefName = prefName
//	var keyName = keyName
//	val newKeys = convertToNewKey(prefName, keyName)
//	prefName = newKeys.first
//	keyName = newKeys.second
//	UserSessionMap.map[Pair(prefName, keyName)] = null
//	internalSetString(prefName, keyName, null)
    }

    private fun internalGetString(prefName: String?, keyName: String?, defValue: String): String? {

	val sharedPrefs = context?.getSharedPreferences(prefName, Context.MODE_PRIVATE)
	return sharedPrefs?.getString(keyName, defValue)
    }

    protected fun setString(prefName: String?, keyName: String?, value: String?) {
	try {
	    if (keyName != null && value != null) {
//		UserSessionKeyMapper.mapUserSessionKeyString(keyName, userSessionDataStore, value)
	    }
	} catch (e: Exception) {
	    // Log here
	}
	var prefName = prefName
	var keyName = keyName
	var value = value
	val newKeys = convertToNewKey(prefName, keyName)
	prefName = newKeys.first
	keyName = newKeys.second
	if(value != null) {
	    UserSessionMap.map[Pair(prefName, keyName)] = value
	}
	value = EncoderDecoder.Encrypt(value, UserSession.KEY_IV) // encrypt string here
	internalSetString(prefName, keyName, value)
    }

    private fun internalSetString(prefName: String?, keyName: String, value: String) {
	val sharedPrefs = context?.getSharedPreferences(prefName, Context.MODE_PRIVATE)
	val editor = sharedPrefs?.edit()
	editor?.putString(keyName, value)
	editor?.apply()
    }

    protected fun getAndTrimOldString(
	prefName: String?,
	keyName: String?,
	defValue: String
    ): String? {
	val newPrefName = String.format("%s%s", prefName, suffix)
	val newKeyName = String.format("%s%s", keyName, suffix)
	val debug = ""
	var isGettingFromMap = false
	val key = Pair(newPrefName, newKeyName)
	if (UserSessionMap.map.containsKey(key)) {
	    try {
		val value = UserSessionMap.map[key]
		return if (value == null) {
		    isGettingFromMap = true
		    defValue
		} else {
		    isGettingFromMap = true
		    value as String?
		}
	    } catch (ignored: Exception) {
	    }
	}
	return try {
	    val oldValue = internalGetString(prefName, keyName, defValue)
	    if (oldValue != null && oldValue != defValue) {
		internalCleanKey(prefName, keyName)
		internalSetString(
		    newPrefName,
		    newKeyName,
		    EncoderDecoder.Encrypt(oldValue, UserSession.KEY_IV)
		)
		UserSessionMap.map[key] = oldValue
		return oldValue
	    }
	    val sharedPrefs = context?.getSharedPreferences(newPrefName, Context.MODE_PRIVATE)
	    var value = sharedPrefs?.getString(newKeyName, defValue)
	    if (value != null) {
		if (value == defValue) { // if value same with def value\
		    value
		} else {
		    value = EncoderDecoder.Decrypt(value, UserSession.KEY_IV) // decrypt here
		    if (value.isEmpty()) {
			defValue
		    } else {
			UserSessionMap.map[key] = value
			value
		    }
		}
	    } else {
		defValue
	    }
	} catch (e: Exception) {
	    defValue
	}
    }

    private fun internalGetBoolean(
	prefName: String?,
	keyName: String?,
	defValue: Boolean
    ): Boolean {
	val sharedPrefs = context?.getSharedPreferences(prefName, Context.MODE_PRIVATE)
	return sharedPrefs?.getBoolean(keyName, defValue) ?: defValue
    }

    private fun internalSetBoolean(prefName: String, keyName: String, value: Boolean) {
	val sharedPrefs = context?.getSharedPreferences(prefName, Context.MODE_PRIVATE)
	val editor = sharedPrefs?.edit()
	editor?.putBoolean(keyName, value)
	editor?.apply()
    }

    protected fun setBoolean(prefName: String?, keyName: String?, value: Boolean) {
	try {
	    if (keyName != null) {
//		UserSessionKeyMapper.mapUserSessionKeyBoolean(keyName, userSessionDataStore, value)
	    }
	} catch (e: Exception) {
	    // Log Here
	}
	var prefName = prefName
	var keyName = keyName
	val newKeys = convertToNewKey(prefName, keyName)
	prefName = newKeys.first
	keyName = newKeys.second
	UserSessionMap.map[Pair(prefName, keyName)] = value
	internalSetBoolean(prefName, keyName, value)
    }

    protected fun getAndTrimOldBoolean(
	prefName: String?,
	keyName: String?,
	defValue: Boolean
    ): Boolean {
	val newPrefName = String.format("%s%s", prefName, suffix)
	val newKeyName = String.format("%s%s", keyName, suffix)
	val key = Pair(newPrefName, newKeyName)
	if (UserSessionMap.map.containsKey(key)) {
	    try {
		val value = UserSessionMap.map[key]
		return if (value == null) {
		    defValue
		} else {
		    value as Boolean
		}
	    } catch (ignored: Exception) {
	    }
	}
	val oldValue = internalGetBoolean(prefName, keyName, defValue)
	if (oldValue != defValue) {
	    internalCleanKey(prefName, keyName)
	    internalSetBoolean(newPrefName, newKeyName, oldValue)
	    UserSessionMap.map[key] = oldValue
	    return oldValue
	}
	val sharedPrefs = context?.getSharedPreferences(newPrefName, Context.MODE_PRIVATE)
	val value = sharedPrefs?.getBoolean(newKeyName, defValue) ?: defValue
	UserSessionMap.map[key] = value
	return value
    }

    companion object {
	const val suffix = "_v2"
    }

    init {
	context = context?.applicationContext
    }
}