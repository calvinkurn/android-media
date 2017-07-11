import React, { Component } from 'react'
import { bindActionCreators } from 'redux'
import { connect } from 'react-redux'
import LoadMore from '../components/LoadMore'
import {
  fetchBrands,
  slideBrands,
  addToFavourite
} from '../actions/actions'
import BrandList from '../components/brandList'

class BrandContainer extends Component {
  componentDidMount() {
    const { offset, limit } = this.props.brands.pagination
    this.props.loadMore(limit, offset)
  }

  render() {
    const { offset, limit } = this.props.brands.pagination
    const totalBrands = this.props.brands.totalBrands
    const totalItemsCount = this.props.brands.items.length
    const isFetching = this.props.brands.isFetching
    let canFetch = true
    if ((totalBrands != 0) && (totalBrands == totalItemsCount)) {
      canFetch = false
    }

    const bannerListProps = {
      brands: this.props.brands.items,
      gridData: this.props.brands.grid.data,
      offset,
      limit,
      canFetch,
      isFetching,
      loadMore: this.props.loadMore,
      slideMore: this.props.slideMore
    }

    return (
      <BrandList {...bannerListProps} />
    )
  }
}

const mapStateToProps = (state, ownProps) => {
  const brands = state.brands
  return {
    brands
  }
}

const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    loadMore: bindActionCreators(fetchBrands, dispatch),
    slideMore: bindActionCreators(slideBrands, dispatch),
    addShopToFav: bindActionCreators(addToFavourite, dispatch),
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(BrandContainer)