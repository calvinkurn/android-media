package com.tokopedia.contactus.inboxticket2.handlingListTags

interface Marker

class BulletedListMark : Marker

class NumberedListMark(val index: Int) : Marker