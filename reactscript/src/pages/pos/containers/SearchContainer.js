import React, { Component } from 'react'
import { connect } from 'react-redux'
import SearchScreen from '../components/SearchScreen'
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
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    onSearch: (text) => {
      dispatch(fetchSearchProduct(text))
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