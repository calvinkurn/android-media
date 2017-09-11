import React, { Component } from 'react'
import {
  ScrollView,
  View,
  NetInfo,
  Text,
  Button,
  StyleSheet,
  TouchableHighlight,
  ActivityIndicator,
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
import NotConnect from '../NotConnect'

import { NavigationModule } from 'NativeModules'


class App extends Component {
  constructor(props) {
    super(props);
    this.state = { showBtn: false, refreshing: false, statusConnection: '', }
  }

  componentWillMount(){
    const { dispatch } = this.props
    NavigationModule.getCurrentUserId().then(uuid => dispatch(reloadState()))

    NetInfo.fetch().then((reach) => {
      this.setState({ statusConnection: reach })
    })
    NetInfo.addEventListener('change', (res) => {
      this.setState({ statusConnection: res }) 
    });
  }


  componentWillUnmount(){
    NetInfo.removeEventListener('change')
  }


  _onRefresh() {
    this.setState({ refreshing: true });
    const { dispatch } = this.props

    NavigationModule.getCurrentUserId().then(uuid => {
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
    const statusConnection = this.state.statusConnection
    if (statusConnection === 'MOBILE' || statusConnection === 'WIFI'){
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
    } else if (statusConnection === 'NONE') {
      return <NotConnect />
    } else {
      return (
        <View style={{ marginTop: 20, justifyContent: 'center', alignItems: 'center', flex: 1 }}>
          <ActivityIndicator size="large" />
        </View>
      )
    }
  }
}

export default connect()(App) 