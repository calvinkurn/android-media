import React, { Component } from 'react'
import { connect } from 'react-redux'
import SearchScreen from '../components/search/SearchScreen'
import {
  fetchSearchProduct,
  onSearchResultTap,
  clearSearchResults,
  onSearchQueryType,
} from '../actions/index'

const mapStateToProps = (state, ownProps) => {
  return {
    visible: ownProps.visible,
    onBackPress: ownProps.onBackPress,
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
  }
}

const SearchContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(SearchScreen)

export default SearchContainer