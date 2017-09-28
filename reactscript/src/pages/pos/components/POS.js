import React, { Component } from 'react'
import {
  StyleSheet,
  // Text,
  View,
  // DrawerLayoutAndroid,
  // ToolbarAndroid,
  // Dimensions,
  // TextInput,
  // TouchableNativeFeedback,
  // TouchableWithoutFeedback
} from 'react-native'
import VisibleProductList from '../containers/VisibleProductList'
import Ticker from '../components/product/Ticker'
import CartContainer from '../containers/CartContainer'
import SearchContainer from '../containers/SearchContainer'
// import PasswordPopup from './PasswordPopup'

export default class POS extends Component {
  constructor() {
    super()
    this.state = {
      cartOpen: false,
      searchOpen: false
    }
  }
  onActionSelected = (position) => {
    if (position === 1) {
      console.log('onActionSelected called')
      this.setState({
        cartOpen: true
      })
    } else {
      this.setState({
        searchOpen: true
      })
    }
  }

  onIconClick = () => {
    this.drawerPane.openDrawer()
  }

  render() {
    return (
        <View style={styles.container}>
          <Ticker />
          <VisibleProductList />
          {/* <PasswordPopup
            navigation={this.props.navigation}
            ref={(passwordPopup) => { this.passwordPopup = passwordPopup; }} /> */}
        </View>
    )
  }
}
const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f1f1f1',
    alignItems: 'center',
  }
})