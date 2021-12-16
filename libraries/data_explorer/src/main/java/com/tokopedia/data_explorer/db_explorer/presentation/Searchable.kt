package com.tokopedia.data_explorer.db_explorer.presentation

interface Searchable {

    fun search(query: String?)
    fun searchQuery(): String?

}