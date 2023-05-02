package com.tokopedia.tokochat.stub.common

import android.content.Context
import android.content.SharedPreferences
import com.tokochat.tokochat_config_common.di.qualifier.TokoChatQualifier
import javax.inject.Inject

class ConversationsPreferencesStub @Inject constructor(
    @TokoChatQualifier context: Context
) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(CONVERSATIONS_PREFERENCES, Context.MODE_PRIVATE)

    var profileId: String?
        get() = sharedPreferences.getString(PROFILE_ID, null)
        set(value) = sharedPreferences.edit().putString(PROFILE_ID, value).apply()

    var persistContactsStatus: Boolean
        get() = sharedPreferences.getBoolean(SHOULD_PERSIST_CONTACTS, true)
        set(value) = sharedPreferences.edit().putBoolean(SHOULD_PERSIST_CONTACTS, value).apply()

    var wsConnectionUrl: String?
        get() = sharedPreferences.getString(WEBSOCKET_CONNECTION_URL, "")
        set(value) = sharedPreferences.edit().putString(WEBSOCKET_CONNECTION_URL, value).apply()

    var wsAuthToken: String?
        get() = sharedPreferences.getString(WEBSOCKET_AUTH_TOKEN, "")
        set(value) = sharedPreferences.edit().putString(WEBSOCKET_AUTH_TOKEN, value).apply()

    var previousChannelsFetched: Boolean
        get() = sharedPreferences.getBoolean(PREVIOUS_CHANNELS_FETCHED, false)
        set(value) = sharedPreferences.edit().putBoolean(PREVIOUS_CHANNELS_FETCHED, value).apply()

    var lastContactSyncTimeStamp: Long
        get() = sharedPreferences.getLong(LAST_CONTACT_SYNC_TIME, 0)
        set(value) = sharedPreferences.edit().putLong(LAST_CONTACT_SYNC_TIME, value).apply()

    fun setProfileDetails(profileId: String) {
        val preferenceEditor = sharedPreferences.edit()
        preferenceEditor.putString(PROFILE_ID, profileId)
        preferenceEditor.apply()
    }

    fun clearProfileData() {
        val preferenceEditor = sharedPreferences.edit()
        preferenceEditor.remove(PROFILE_ID)
        preferenceEditor.remove(PUSH_TOKEN)
        preferenceEditor.remove(SHOULD_PERSIST_CONTACTS)
        preferenceEditor.remove(PREVIOUS_CHANNELS_FETCHED)
        preferenceEditor.remove(LAST_CONTACT_SYNC_TIME)
        preferenceEditor.apply()
    }

    fun setBackgroundContactsSyncingCompleted() {
        sharedPreferences.edit().putBoolean(CONTACTS_SYNCING, true).apply()
    }

    fun getBackgroundContactsSyncingStatus(): Boolean {
        return sharedPreferences.getBoolean(CONTACTS_SYNCING, false)
    }

    fun clearBackgroundContactsSyncingStatus() {
        sharedPreferences.edit().remove(CONTACTS_SYNCING).apply()
    }

    fun areProfileDetailsPresent(): Boolean {
        return profileId.isNullOrEmpty().not()
    }

    companion object {
        private const val CONVERSATIONS_PREFERENCES = "ConversationsPreferences"
        private const val PROFILE_ID = "ProfileId"
        private const val PUSH_TOKEN = "PushToken"
        private const val CONTACTS_SYNCING = "ContactsSyncing"
        private const val SHOULD_PERSIST_CONTACTS = "shouldPersistContacts"
        private const val PREVIOUS_SESSION_BABBLE_STATE = "previousSessionBabbleState"
        private const val WEBSOCKET_CONNECTION_URL = "webSocketConnectionUrl"
        private const val WEBSOCKET_AUTH_TOKEN = "webSocketAuthToken"
        private const val PREVIOUS_CHANNELS_FETCHED = "AllPreviousChannelFetched"
        private const val LAST_CONTACT_SYNC_TIME = "LastContactSyncTimeStamp"
    }
}
