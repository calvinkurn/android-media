import React, { Component } from 'react'
import {
  Platform,
  StyleSheet,
  Text,
  View
} from 'react-native'
import { Provider } from 'react-redux'
import store from './store/Store'
import App from './components/App'
import TermsConditions from './components/TermsConditions'


class Root extends Component {
    render() {
        if (this.props.data.SubPage === 'promo-terms'){
            return (
                <Provider store={store}>
                    <TermsConditions data={this.props.data} />
                </Provider>
            )
        } else {
            return (
                <Provider store={store}>
                    <App screenProps={this.props.data} />
                </Provider>
            )
        }
    }
}

export default Root