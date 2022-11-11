package com.tokopedia.tokomember_seller_dashboard.model

interface DataModel

data class UserCardMemberModel(
    val member : UserCardMember? = null
) : DataModel

class MemberListInfiniteLoader : DataModel