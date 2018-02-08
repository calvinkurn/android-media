import React, { Component } from 'react'
import { connect } from 'react-redux'
import SearchScreen from '../components/pages/search/SearchBar'
import {
  clearSearchResults,
  searchProduct,
  onSearchQueryType,
  setSearchText,
  resetProductList,
  fetchProducts,
  searchProductSubmit,
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
    onSearch: (text, eid) => {
      const trimText = text.trim() || ''
      if (trimText) {
        dispatch(searchProduct(trimText, eid))
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
      dispatch(searchProductSubmit(text, eid))
    }
  }
}

const SearchContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(SearchScreen)

export default SearchContainer