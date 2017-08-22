import React, { Component } from 'react'
import { DeviceEventEmitter, AsyncStorage } from 'react-native'
import { bindActionCreators } from 'redux'
import { connect } from 'react-redux'
import LoadMore from '../components/LoadMore'
import {
  fetchBrands,
  slideBrands,
  addToFavourite,
  addToFavouritePdp,
  removeFavoritePdp,
  addWishlistFromPdpBrands,
  removeWishlistPdpBrands,
  resetBrands
} from '../actions/actions'
import BrandList from '../components/brandList'


class BrandContainer extends Component {
  componentWillMount() {
    const { offset, limit } = this.props.brands.pagination
    const { User_ID } = this.props.screenProps
    this.props.loadMore(limit, offset, User_ID)

    this.addToWishlist = DeviceEventEmitter.addListener('WishlistAdd', (res) => {
      this.props.brandAddWishlistPdp(res)
    })
    this.removeWishlist = DeviceEventEmitter.addListener('WishlistRemove', (res) => {
      this.props.brandRemoveWishlistPdp(res)
    })
    this.addToFavoritePDP = DeviceEventEmitter.addListener('FavoriteAdd', (res) => {
      this.props.addFavouriteFromPDP(res.shop_id)
    })
    this.removeFavoritePDP = DeviceEventEmitter.addListener('FavoriteRemove', (res) => {
      this.props.removeFavoriteFromPdp(res.shop_id)
    })

    this.checkLogin = DeviceEventEmitter.addListener('Login', (res) => {
      console.log(res, 'brandContainer Login')
      // const userid_from_login_os = res.user_id
      // AsyncStorage.setItem('user_id_brands', res.user_id);
      
      AsyncStorage.getItem('user_id')
      .then(uid => {
          console.log(uid, typeof(uid))
          // dispatch(fetchCampaigns(uid))
          this.props.resetBrandsAfterLogin(limit, offset, uid, 'REFRESH')
      })
      // const userid_from_login_os = res.user_id
      // this.props.resetBrandsAfterLogin(limit, offset, userid_from_login_os, 'REFRESH')
    })
    console.log('Mount BrandContainer.js')
  }

  componentWillUnmount(){
    console.log('WillUnmount BrandContainer')
    this.addToWishlist.remove()
    this.removeWishlist.remove()
    this.addToFavoritePDP.remove()
    this.removeFavoritePDP.remove()
    this.checkLogin.remove()
    // this.props.loadMore(10, 0, User_ID, 'REFRESH')
  }

  render() {
    const { offset, limit } = this.props.brands.pagination
    const { status, isFetching } = this.props.brands
    const totalBrands = this.props.brands.totalBrands
    const totalItemsCount = this.props.brands.items.length
    const totalItems = this.props.brands.status === 'LOADANDREPLACE' ? this.props.brands.grid.data : this.props.brands.items
    let canFetch = true
    if ((totalBrands != 0) && (totalBrands == totalItemsCount)) {
      canFetch = false
    }

    const bannerListProps = {
      status: this.props.brands.status,
      brands: totalItems,
      gridData: this.props.brands.grid.data,
      offset,
      limit,
      canFetch,
      isFetching,
      loadMore: this.props.loadMore,
      slideMore: this.props.slideMore,
      // User_ID: this.props.screenProps.User_ID
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
    addFavouriteFromPDP: bindActionCreators(addToFavouritePdp, dispatch),
    removeFavoriteFromPdp: bindActionCreators(removeFavoritePdp, dispatch),
    brandAddWishlistPdp: bindActionCreators(addWishlistFromPdpBrands, dispatch),
    brandRemoveWishlistPdp: bindActionCreators(removeWishlistPdpBrands, dispatch),
    resetBrandsAfterLogin: bindActionCreators(resetBrands, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(BrandContainer)