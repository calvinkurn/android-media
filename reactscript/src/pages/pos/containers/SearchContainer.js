import React, { Component } from 'react'
import { connect } from 'react-redux'
//import SearchScreen from '../components/search/SearchScreen'
import SearchScreen from '../components/search/SearchBar'
import {
  fetchSearchProduct,
  onSearchResultTap,
  clearSearchResults,
  onSearchQueryType,
  fetchProducts,
  resetProductList,
  setSearchText,
  onSubmitFetchSearchProduct,
} from '../actions/index'

const mapStateToProps = (state, ownProps) => {
  return {
    items: state.search.items,
    queryText: state.search.query,
    isFetching: state.search.isFetching,
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    onSearch: (text) => {
      const trimText = text.trim() || ''
      if (trimText) {
        dispatch(fetchSearchProduct(trimText))
      }
    },
    onClearSearch: () => {
      dispatch(clearSearchResults())
    },
    onSearchType: (text) => {
      dispatch(onSearchQueryType(text))
    },
    onSearchItemTap: (p) => {
      dispatch(setSearchText(p.text))
      dispatch(resetProductList())
      dispatch(fetchProducts(1987772, 0, 25, 0, p.id))
    },
    onSubmit: (text) => {
      dispatch(resetProductList())
      dispatch(onSubmitFetchSearchProduct(text))
    }
  }
}

const SearchContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(SearchScreen)

export default SearchContainer