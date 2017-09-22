import React, { Component } from 'react'
import { View, ActivityIndicator } from 'react-native'
import { NavigationModule } from 'NativeModules'
import { connect } from 'react-redux'
import { fetchBanners } from '../actions/actions'
import BannerList from '../components/bannerList'

class BannerContainer extends Component {
  componentDidMount() {
    const { dispatch } = this.props
    dispatch(fetchBanners())
  }

  onBannerPress = (e, banner) => {
    //TODO: Add GTM Event.
    NavigationModule.navigateWithMobileUrl(banner.redirect_url_app, banner.redirect_url, "")
  }

  onViewAllPress = (e, banner) => {
    //TODO: Add GTM Event.
    NavigationModule.navigateWithMobileUrl('', 'https://www.tokopedia.com/promo/category/official-store', "")
  }

  render() {
    const banners = this.props.banners.items
    return (
      this.props.banners.isFetching ? 
        <View style={{ marginTop:20, marginBottom:20, justifyContent:'center', alignItems:'center', flex:1}}>
          <ActivityIndicator size="large" /></View> : 
        <BannerList
          banners={banners}
          onBannerPress={this.onBannerPress}
          onViewAllPress={this.onViewAllPress} />
    )
  }
}

const mapStateToProps = state => {
  const banners = state.banners
  return {
    banners
  }
}

export default connect(mapStateToProps)(BannerContainer)