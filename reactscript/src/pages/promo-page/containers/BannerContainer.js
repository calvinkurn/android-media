import React, { Component } from 'react'
import {
  View,
  Text
} from 'react-native'
import { connect } from 'react-redux'
import { fetchTopBanner } from '../actions'
import TopBanner from '../components/TopBanner'
import MainBanner from '../components/MainBanner'



class BannerContainer extends Component {
  componentDidMount() {
    const { dispatch, dataSlug } = this.props
    dispatch(fetchTopBanner(dataSlug))
  }

  render() {
    const banners = this.props.TopBanners.items
    if (banners){
       return (
          <View style={{backgroundColor: '#F8F8F8'}}>
            <TopBanner 
              navigation={this.props.navigation}
              dataTopBanners={banners}
            />
            <MainBanner dataMainBanners={banners} />
          </View>
      )
    } 

    return null
  }
}

const mapStateToProps = (state) => {
  const TopBanners = state.banners
  return {
    TopBanners
  }
}

export default connect(mapStateToProps)(BannerContainer)