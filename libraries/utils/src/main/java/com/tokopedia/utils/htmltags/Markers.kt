package com.tokopedia.utils.htmltags

interface Marker

class BulletedListMark : Marker

class NumberedListMark(val index: Int) : Marker