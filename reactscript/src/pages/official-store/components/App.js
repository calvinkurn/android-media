import React, { Component } from 'react'
import { ScrollView, View, Text, Button, StyleSheet, TouchableHighlight, Dimensions } from 'react-native'
import BannerContainer from '../containers/bannerContainer'
import CampaignContainer from '../containers/campaignContainer'
import BrandContainer from '../containers/brandContainer'
import Infographic from '../components/infographic'
import BackToTop from '../common/BackToTop/backToTop'
import Seo from '../components/seo'

export default class App extends Component {
  constructor(props) {
    super(props);
    this.state = { showBtn: false };
  }
  onBackToTopTap = (event) => {
    var currentOffset = event.nativeEvent
    console.log(currentOffset)
    this.refs.scrollView.scrollTo({ x: 0, y: 0, animatd: true });
  }

  onScroll = (event) => {
    if (event.nativeEvent.contentOffset.y > 800) {
      this.setState({
        showBtn: true
      })
    } else {
      this.setState({
        showBtn: false
      })
    }
  }

  render() {
    return (
      <View>
        <ScrollView ref="scrollView" onScroll={this.onScroll}>
           <BannerContainer /> 
          <CampaignContainer />
           <BrandContainer /> 
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