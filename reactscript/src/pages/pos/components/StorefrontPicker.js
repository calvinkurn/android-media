import React, { Component } from 'react'
import { Picker, StyleSheet } from 'react-native'
import PropTypes from 'prop-types'

export default class StorefrontPicker extends Component {
  render() {
    const { value, onChange, options, isFetching } = this.props
    const val = value.toString()
    return (
      <Picker
        style={styles.container}
        onValueChange={onChange}
        enabled={!isFetching}
        selectedValue={val}>
        {
          options.map(e => <Picker.Item label={e.name} value={e.id.toString()} key={e.id} />)
        }
      </Picker>
    )
  }
}

StorefrontPicker.propTypes = {
  options: PropTypes.array.isRequired,
  value: PropTypes.string.isRequired,
  onChange: PropTypes.func.isRequired
}

const styles = StyleSheet.create({
  container: {
    width: 200,
    height: 30,
    backgroundColor: '#fff',
  }
})