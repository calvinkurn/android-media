import React, { Component } from 'react'
import { connect } from 'react-redux'
import SearchScreen from '../components/SearchScreen'

const mapStateToProps = (state, ownProps) => {
  return {
    visible: ownProps.visible,
    onBackPress: ownProps.onBackPress,
  }
}

const mapDispatchToProps = (dispatch) => {
  return {

  }
}

const SearchContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(SearchScreen)

export default SearchContainer