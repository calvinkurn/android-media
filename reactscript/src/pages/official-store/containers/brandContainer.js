import React, { Component } from 'react'
import { DeviceEventEmitter } from 'react-native'
import { bindActionCreators } from 'redux'
import { connect } from 'react-redux'
import LoadMore from '../components/LoadMore'
import {
  fetchBrands,
  slideBrands,
  addToFavourite,
  addToFavouritePdp,
  removeFavoritePdp
} from '../actions/actions'
import BrandList from '../components/brandList'


class BrandContainer extends Component {
  componentWillMount() {
    const { offset, limit } = this.props.brands.pagination
    const { User_ID } = this.props.screenProps
    this.props.loadMore(limit, offset, User_ID)

    // this.addToWishlist = DeviceEventEmitter.addListener('WishlistAdd', (res) => {
    //   console.log(res)
    //   this.props.loadMore(10, 0, User_ID, 'REFRESH')
    //   // fetchBrands(10, 0, User_ID, 'REFRESH')
    // })
    // this.removeWishlist = DeviceEventEmitter.addListener('WishlistRemove', (res) => {
    //   console.log(res)
    //   this.props.loadMore
    // })
    this.addToFavoritePDP = DeviceEventEmitter.addListener('FavoriteAdd', (res) => {
      console.log(res)
      this.props.addFavouriteFromPDP(res.shop_id)
    })
    this.removeFavoritePDP = DeviceEventEmitter.addListener('FavoriteRemove', (res) => {
      console.log(res)
      this.props.removeFavoriteFromPdp(res.shop_id)
    })
  }


  componentWillUnmount(){
    this.addToWishlist.remove()
    this.removeWishlist.remove()
    this.addToFavoritePDP.remove()
    this.removeFavoritePDP.remove()
  }



  render() {
    const { offset, limit } = this.props.brands.pagination
    const totalBrands = this.props.brands.totalBrands
    const totalItemsCount = this.props.brands.items.length
    // const totalItems = this.props.brands.items
    const totalItems = this.props.brands.status === 'LOADANDREPLACE' ? this.props.brands.grid.data : this.props.brands.items
    const isFetching = this.props.brands.isFetching
    let canFetch = true
    if ((totalBrands != 0) && (totalBrands == totalItemsCount)) {
      canFetch = false
    }

    const bannerListProps = {
      brands: totalItems,
      gridData: this.props.brands.grid.data,
      offset,
      limit,
      canFetch,
      isFetching,
      loadMore: this.props.loadMore,
      slideMore: this.props.slideMore,
      User_ID: this.props.screenProps.User_ID
    }

    console.log(bannerListProps)

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
    addFavouriteFromPDP: bindActionCreators(addToFavouritePdp, dispatch),
    removeFavoriteFromPdp: bindActionCreators(removeFavoritePdp, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(BrandContainer)
