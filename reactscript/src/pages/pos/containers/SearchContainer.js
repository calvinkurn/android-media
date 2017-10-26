import React, { Component } from 'react'
import { connect } from 'react-redux'
//import SearchScreen from '../components/search/SearchScreen'
import SearchScreen from '../components/pages/search/SearchBar'
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
    etalaseId: state.etalase.selected,
    shopId: ownProps.shopId,
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    onSearch: (text, eid, shopId) => {
      const trimText = text.trim() || ''
      if (trimText) {
        dispatch(fetchSearchProduct(eid, trimText, shopId))
      }
    },
    onClearSearch: () => {
      dispatch(clearSearchResults())
    },
    onSearchType: (text) => {
      dispatch(onSearchQueryType(text))
    },
    onSearchItemTap: (p, shopId) => {
      dispatch(setSearchText(p.text))
      dispatch(resetProductList())
      dispatch(fetchProducts(shopId, 0, 25, 0, p.id))
    },
    onSubmit: (text, eid, shopId) => {
      dispatch(resetProductList())
      dispatch(onSubmitFetchSearchProduct(text, eid, shopId))
    }
  }
}

const SearchContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(SearchScreen)

export default SearchContainer