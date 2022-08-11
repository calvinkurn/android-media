package com.tokopedia.gamification.data.entity

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokenHomeEntity(
    @SerializedName("buttonApplink")
    @Expose
    var buttonApplink: String = "",

    @SerializedName("buttonURL")
    @Expose
    var buttonURL: String = "",

    @SerializedName("countingMessage")
    @Expose
    var countingMessage: Array<String> = arrayOf(),

    @SerializedName("tokensUser")
    @Expose
    var tokensUser: TokenUserEntity? = null,

    @SerializedName("emptyState")
    @Expose
    var emptyState: TokenEmptyStateEntity? = null,

    @SerializedName("tokenSourceMessage")
    @Expose
    var tokenSourceMessage: Array<String> = arrayOf(),

    @SerializedName("homeActionButton")
    @Expose
    var homeActionButton: List<HomeActionButton> = listOf(),

    @SerializedName("homeSmallButton")
    @Expose
    private val homeSmallButton: HomeSmallButton? = null,
) : Parcelable{

    protected constructor(@SuppressLint("EntityFieldAnnotation") p:Parcel) : this(){
        buttonApplink = p.readString() ?: ""
        buttonURL = p.readString() ?: ""
        countingMessage = p.createStringArray() ?: arrayOf()
        tokenSourceMessage = p.createStringArray() ?: arrayOf()
        tokensUser = p.readParcelable(TokenUserEntity::class.java.classLoader)
        emptyState = p.readParcelable(TokenEmptyStateEntity::class.java.classLoader)
    }


    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(buttonApplink)
        dest?.writeString(buttonURL)
        dest?.writeStringArray(countingMessage)
        dest?.writeStringArray(tokenSourceMessage)
        dest?.writeParcelable(tokensUser, flags)
        dest?.writeParcelable(emptyState, flags)
    }

    companion object{
        @JvmField
        val CREATOR = object : Parcelable.Creator<TokenHomeEntity>{
            override fun createFromParcel(p: Parcel): TokenHomeEntity {
                return TokenHomeEntity(p)
            }

            override fun newArray(size: Int): Array<TokenHomeEntity?> {
                return arrayOfNulls(size)
            }
        }
    }
}
