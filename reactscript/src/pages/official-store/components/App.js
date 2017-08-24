import React, { Component } from 'react'
import {
  ScrollView,
  View,
  Text,
  Button,
  StyleSheet,
  TouchableHighlight,
  Dimensions,
  DeviceEventEmitter,
  AsyncStorage,
  RefreshControl 
} from 'react-native'
import { connect } from 'react-redux'
import BannerContainer from '../containers/bannerContainer'
import CampaignContainer from '../containers/campaignContainer'
import BrandContainer from '../containers/brandContainer'
import Infographic from '../components/infographic'
import BackToTop from '../common/BackToTop/backToTop'
import Seo from '../components/seo'
import OfficialStoreIntro from '../components/OfficialStoreIntro'
import { fetchBanners, fetchCampaigns, fetchBrands, refreshState, reloadState } from '../actions/actions'


class App extends Component {
  constructor(props) {
    super(props);
    this.state = { showBtn: false, refreshing: false, }
  }

  componentWillMount(){
    const { dispatch } = this.props
    if (this.props.screenProps.Screen === 'official-store'){
      AsyncStorage.getItem('user_id')
        .then(uid => {
          dispatch(reloadState())
        })  
    }
  }


  _onRefresh() {
    this.setState({ refreshing: true });
    const { dispatch } = this.props
    AsyncStorage.getItem('user_id')
      .then(uid => {
          dispatch(refreshState())
          dispatch(fetchBanners())
          dispatch(fetchCampaigns(uid))
          dispatch(fetchBrands(10, 0, uid, 'REFRESH'))
      })
    setTimeout(() => {
      this.setState({ refreshing: false });
    }, 5000)
  }
 
  onBackToTopTap = (event) => {
    var currentOffset = event.nativeEvent
    this.refs.scrollView.scrollTo({ x: 0, y: 0, animatd: true });
  }

  onScroll = (event) => {
    if (event.nativeEvent.contentOffset.y > 800) {
      this.setState({ showBtn: true })
    } else {
      this.setState({ showBtn: false })
    }
  }


  render() {
    return (
      <View>
        <ScrollView 
          ref="scrollView" 
          onScroll={this.onScroll}
          refreshControl={
            <RefreshControl
              refreshing={this.state.refreshing}
              onRefresh={this._onRefresh.bind(this)}
              colors={['#42b549']} />
          }>
          <OfficialStoreIntro />
          <BannerContainer screenProps={this.props.screenProps} />
          <CampaignContainer screenProps={this.props.screenProps} />
          <BrandContainer screenProps={this.props.screenProps} />
          <Infographic /> 
          <Seo /> 
        </ScrollView>
        {
          this.state.showBtn ? (<BackToTop onTap={this.onBackToTopTap} />) : null
        }
      </View>
    )
  }
}

export default connect()(App) 