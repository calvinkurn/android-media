package com.tokopedia.groupchat.room.view.viewmodel

/**
 * @author : Steven 25/06/19
 */
class OverflowMenuButtonViewModel(var title: String,
                                  var action:(() -> Unit)? = null,
                                  var image: Int = 0,
                                  var check: Boolean = false
)