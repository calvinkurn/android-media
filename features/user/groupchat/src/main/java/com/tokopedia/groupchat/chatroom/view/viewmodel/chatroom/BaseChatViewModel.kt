package com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.groupchat.common.util.TimeConverter

/**
 * @author by nisie on 2/15/18.
 */

open class BaseChatViewModel : Parcelable {

    var isShowHeaderTime: Boolean = false
    var message: String? = ""
        private set
    var createdAt: Long = 0
        private set
    var updatedAt: Long = 0
        private set
    var formattedCreatedAt: String? = ""
        private set
    var formattedUpdatedAt: String? = ""
        private set
    var messageId: String? = ""
        private set
    private var headerTime: Long = 0
    var formattedHeaderTime: String? = ""
        private set

    var senderId: String? = ""

    var senderName: String? = ""

    var senderIconUrl: String? = ""

    var isInfluencer: Boolean = false

    var isAdministrator: Boolean = false


    internal constructor(message: String, createdAt: Long, updatedAt: Long, messageId: String) {
        this.isShowHeaderTime = false
        this.headerTime = 0
        this.formattedHeaderTime = ""
        this.message = message.replace("\\n", "\n")
        this.createdAt = createdAt
        this.updatedAt = if (updatedAt != 0L) updatedAt else createdAt
        this.formattedCreatedAt = TimeConverter.convertToHourFormat(this.createdAt)
        this.formattedUpdatedAt = TimeConverter.convertToHourFormat(this.updatedAt)
        this.messageId = messageId
        this.senderId = ""
        this.senderName = ""
        this.senderIconUrl = ""
        this.isInfluencer = false
        this.isAdministrator = false
    }

    internal constructor(message: String, createdAt: Long, updatedAt: Long, messageId: String?,
                         senderId: String, senderName: String, senderIconUrl: String,
                         isInfluencer: Boolean, isAdministrator: Boolean) {
        this.isShowHeaderTime = false
        this.headerTime = 0
        this.formattedHeaderTime = ""
        this.message = message.replace("\\n", "\n")
        this.createdAt = createdAt
        this.updatedAt = if (updatedAt != 0L) updatedAt else createdAt
        this.formattedCreatedAt = TimeConverter.convertToHourFormat(this.createdAt)
        this.formattedUpdatedAt = TimeConverter.convertToHourFormat(this.updatedAt)
        this.messageId = messageId
        this.senderId = senderId
        this.senderName = senderName
        this.senderIconUrl = senderIconUrl
        this.isInfluencer = isInfluencer
        this.isAdministrator = isAdministrator
    }

    fun setHeaderTime(headerTime: Long) {
        this.headerTime = headerTime
        this.formattedHeaderTime = TimeConverter.convertToHeaderDisplay(headerTime)
    }

    fun getHeaderTime(): Long {
        return headerTime
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeByte((if (isShowHeaderTime) 1 else 0).toByte())
        dest.writeString(message)
        dest.writeLong(createdAt)
        dest.writeLong(updatedAt)
        dest.writeString(formattedCreatedAt)
        dest.writeString(formattedUpdatedAt)
        dest.writeString(messageId)
        dest.writeLong(headerTime)
        dest.writeString(formattedHeaderTime)
        dest.writeString(senderId)
        dest.writeString(senderName)
        dest.writeString(senderIconUrl)
        dest.writeByte((if (isInfluencer) 1 else 0).toByte())
        dest.writeByte((if (isAdministrator) 1 else 0).toByte())
    }

    protected constructor(`in`: Parcel) {
        isShowHeaderTime = `in`.readByte().toInt() != 0
        message = `in`.readString()
        createdAt = `in`.readLong()
        updatedAt = `in`.readLong()
        formattedCreatedAt = `in`.readString()
        formattedUpdatedAt = `in`.readString()
        messageId = `in`.readString()
        headerTime = `in`.readLong()
        formattedHeaderTime = `in`.readString()
        senderId = `in`.readString()
        senderName = `in`.readString()
        senderIconUrl = `in`.readString()
        isInfluencer = `in`.readByte().toInt() != 0
        isAdministrator = `in`.readByte().toInt() != 0
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<BaseChatViewModel> = object : Parcelable.Creator<BaseChatViewModel> {
            override fun createFromParcel(`in`: Parcel): BaseChatViewModel {
                return BaseChatViewModel(`in`)
            }

            override fun newArray(size: Int): Array<BaseChatViewModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}
